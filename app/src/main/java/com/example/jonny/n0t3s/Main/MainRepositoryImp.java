package com.example.jonny.n0t3s.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

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
    boolean click = true;



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

        fireUser.getEmail();
        noti.setSenderNoti(fireUser.getEmail());
        noti.setMessageNoti("This user is interested in your post");
        user.setUserLike(false);
        notifications.put("from", noti.getSenderNoti());
        notifications.put("message", noti.getMessageNoti());




        myCollection.collection("Notification").document(user.gettimeStampMe()).set(notifications)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Utils.toastMessage("Notification sent", myCont);
                        //Log.d("THIS IS THE TOKEN!!!!", user.getUserToken());




                       getMyToken(user,noti.getSenderNoti(), noti.getMessageNoti());



                        //jsonNotification(noti.getSenderNoti(), noti.getMessageNoti(), fireUser.getUid());



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Utils.toastMessage("Error!!!" + e.toString(), myCont);

                    }
                });




    }


    public void getMyToken(User user, final String titleNoti, final String msgNoti){
        myCollection.collection("Notes").document(user.gettimeStampMe()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {



                        token = (String) document.getString("timeStampMe");
                        String cat;
                        cat =(String)document.getString("title");

                        String notiTitle;
                        notiTitle ="/topics/"+token;
                        Log.d("THISH  THE OTKEN NOW!#", token);





                        jsonNotification(titleNoti, msgNoti, notiTitle);
                        Log.d("Doode Look at here!!!!", fireUser.getUid());
    //                    jsonNotification(titleNoti, msgNoti, notiTitle);


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
            jsonNotiMsg.put("message", NOTI_MESSAGE);

            Log.d("You are in here", NOTI_TITLE);

            jsonNoti.put("to",NOTI_TOPIC);
            Log.d("Look here at me mirame", NOTI_TOPIC);
            jsonNoti.put("data", jsonNotiMsg);
            Log.d("Also Look Here", jsonNotiMsg.toString());
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendJsonNoti(jsonNoti);
    }



    public  void sendJsonNoti(JSONObject userNoti) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( "https://fcm.googleapis.com/fcm/send", userNoti,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response.toString());
                       //String msg = msgNoti+"";
                       //String topic =  notiTopic+"";

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

                params.put("Authorization",myServerKey);
                params.put("Content-Type", jsonContent);
                return params;
            }
        };
        MyRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);


    }



}
