package com.example.jonny.n0t3s.Main;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.annotation.NonNull;

import android.content.SharedPreferences;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


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
    FirebaseAuth mainUser;
    FirebaseFirestore myCollection =  FirebaseFirestore.getInstance();
    User myUser;
    public String token;
    MainActivity myActi;
    public String notiTitle;


    String likeValue;
    int likeCount = 0;

    private static final String sharedPref = "Time";




    public MainRepositoryImp(Context base) {
        super(base);
        myCont = base;
        myActi = new MainActivity();
    }

    @Override
    public void deleteNote(final List<User> userList, final User user,
                           final RecyclerTwoAdapter adapter, final int pos) {


        user.setUserID(fireUser.getUid());
        userPath = user.gettimeStampMe()+user.getTitle();
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



       myCollection.collection("Notes").document(user.gettimeStampMe()+user.getTitle()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {



                        token = (String) document.getString("token");

                        notiTitle =(String)document.getString("title");




                        fireUser.getEmail();
                        noti.setTimeStamp(user.gettimeStampMe());
                        noti.setSenderEmail(fireUser.getEmail());
                        noti.setOwnerName(user.getName());
                        noti.setReceiverName(fireUser.getDisplayName());
                        noti.setSenderNoti(notiTitle);
                        noti.setOwnerEmail(user.getEma());
                        noti.setMoneyAmount(user.getMoneyAmount());
                        noti.setMessageNoti(fireUser.getEmail()+" "+ "is interested in your post.");
                        user.setUserLike(false);
                        notifications.put("from", noti.getSenderNoti());
                        notifications.put("message", noti.getMessageNoti());
                        notifications.put("senderEmail", noti.getSenderEmail());
                        notifications.put("timeStamp", noti.getTimeStamp());
                        notifications.put("ownerEmail", noti.getOwnerEmail());
                        notifications.put("money", noti.getMoneyAmount());
                        notifications.put("ownerName", noti.getOwnerName());
                        notifications.put("receiverName", noti.getReceiverName());

                      //myAdapter.getData(noti.getTimeStamp());



                        setTime(noti.getTimeStamp());





                        myCollection.collection("ApplicantsOf"+user.getEma()).document(fireUser.getEmail()+user.gettimeStampMe()).set(notifications)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Utils.toastMessage("Notification was sent to post owner", myCont);
                                        //Log.d("THIS IS THE TOKEN!!!!", user.getUserToken());





                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Utils.toastMessage("Error!!!" + e.toString(), myCont);

                                    }
                                });






                        //send to personal applications
                        myCollection.collection("personalApplications"+fireUser.getEmail())
                                .document(user.gettimeStampMe()).set(notifications)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                       // Utils.toastMessage("Notification sent to post owner", myCont);
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












    public void setTime(String timeStamp){

        SharedPreferences settings = myCont.
                getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("time", timeStamp);
        editor.commit();
    }









}
