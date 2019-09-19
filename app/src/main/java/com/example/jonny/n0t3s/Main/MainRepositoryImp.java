package com.example.jonny.n0t3s.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

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
        Map<String, Object> notifications = new HashMap<>();
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

                        //get token method
                        //getMyToken(user);


                       getMyToken(user);




                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Utils.toastMessage("Error!!!" + e.toString(), myCont);

                    }
                });




    }


    public void getMyToken(User user){
        myCollection.collection("Notes").document(user.gettimeStampMe()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {



                        token = (String) document.getString("token");
                        Log.d("THISH  THE OTKEN NOW!#", token);

                        sendNotification(token);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //return token;
    }

    public void sendNotification(final String myToken){


        FirebaseMessaging.getInstance().subscribeToTopic(myToken)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            //  msg = getString(R.string.msg_subscribe_failed);
                        }
                        // Log.d(TAG, msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
