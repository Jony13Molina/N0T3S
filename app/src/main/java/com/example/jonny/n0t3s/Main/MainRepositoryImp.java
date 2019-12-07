package com.example.jonny.n0t3s.Main;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.annotation.NonNull;

import android.content.res.AssetFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jonny.n0t3s.BuildConfig;
import com.example.jonny.n0t3s.Notification;
import com.example.jonny.n0t3s.User;
import com.example.jonny.n0t3s.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainRepositoryImp extends ContextWrapper implements MainRepository {

    Context myCont;
    FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
    String userPath;
    String pathId;
    FirebaseFirestore myCollection =  FirebaseFirestore.getInstance();
    int likeCount;
    User myUser;
    public String token;
    MainActivity myActi;
    public String notiTitle;



    final private String myServerKey = BuildConfig.ApiKey;
    final private String jsonContent = "application/json";
    final private String FCM_ADD = BuildConfig.FcmAdress;



    public MainRepositoryImp(Context base) {
        super(base);
        myCont = base;
        myActi = new MainActivity();
    }

    @Override
    public void deleteNote(final List<User> userList, final User user,
                           final RecyclerTwoAdapter adapter, final int pos) {


        user.setUserID(fireUser.getUid());
        userPath = user.gettimeStampMe();
        pathId = "Notes";





        myCollection.collection(pathId).document(userPath).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        myActi.deleteNote(userList, pos, adapter, myCont);
                    }
                });
        Log.d("DDDD", user.getUserID());
    }

    @Override
    public void setLike(final User user) {


        user.setUserID(fireUser.getUid());



        //make sure the person who made the note or post
        //doesn't like their own note/post
        //then check for the click and add/subtract accordingly
        if (user.getUserID().equals(fireUser.getUid())) {




            //myActi.updateMyLike(user);
        }


    }



    @Override
    public void updateLike(String id, String path, String likeVal, boolean likeState) {

        myCollection = FirebaseFirestore.getInstance();

        myCollection.collection(id).document(path).update("likeCounter", likeVal);
        myCollection.collection(id).document(path).update("userLike", likeState);


    }

    @Override
    public FirebaseFirestore getMyData() {
        return myCollection =  FirebaseFirestore.getInstance();

    }

    @Override
    public void storeNotification(final Notification noti, final User user){
        final Map<String, Object> notifications = new HashMap<>();
        //notes.put("userID",mainUser.getUid());



        myCollection.collection("Notes").document(user.gettimeStampMe()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {



                        token = (String) document.getString("token");

                        notiTitle =(String)document.getString("title");




                        fireUser.getEmail();
                        noti.setSenderNoti("Post:"+notiTitle);
                        noti.setMessageNoti(fireUser.getEmail()+" "+ "is interested in your post.");
                        user.setUserLike(false);
                        notifications.put("from", noti.getSenderNoti());
                        notifications.put("message", noti.getMessageNoti());

                        jsonNotification(noti.getSenderNoti(), noti.getMessageNoti(),token);


                        myCollection.collection("Notification").document(user.gettimeStampMe()).set(notifications)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Utils.toastMessage("Notification sent", myCont);
                                        //Log.d("THIS IS THE TOKEN!!!!", user.getUserToken());





                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Utils.toastMessage("Error!!!" + e.toString(), myCont);

                                    }
                                });



                    } else {
                        Log.d(TAG, "No such document");
                      }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });








    }




    public void jsonNotification(String NOTI_TITLE, String NOTI_MESSAGE, String NOTI_TOPIC){


        JSONObject jsonNoti = new JSONObject();
        JSONObject jsonNotiMsg = new JSONObject();

        Log.d("this is the title!!!", NOTI_TITLE);

        try {
            jsonNotiMsg.put("title", NOTI_TITLE);
            jsonNotiMsg.put("body", NOTI_MESSAGE);

            Log.d("You are in here", NOTI_TITLE);

            jsonNoti.put("to",NOTI_TOPIC);

            jsonNoti.put("notification", jsonNotiMsg);
            Log.d("Also Look Here", jsonNotiMsg.toString());
            Log.d("This is the json noti", jsonNoti.toString());
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendJsonNoti(jsonNoti);
    }



    public  void sendJsonNoti(JSONObject userNoti) {

        Log.d("this us the noti", userNoti.toString());



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_ADD, userNoti,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response.toString());


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText( getApplicationContext(), "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String>  getHeaders() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();


                    params.put("Authorization", myServerKey);

                params.put("Content-Type", jsonContent);
                return params;
            }
        };
        MyRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);


    }









}
