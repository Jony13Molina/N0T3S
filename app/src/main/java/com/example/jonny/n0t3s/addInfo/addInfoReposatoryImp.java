package com.example.jonny.n0t3s.addInfo;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.example.jonny.n0t3s.User;
import com.example.jonny.n0t3s.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import static com.example.jonny.n0t3s.addInfo.UI.addInfo.pToastMessage;
import static com.example.jonny.n0t3s.addInfo.UI.addInfo.privateMessage;
import static com.example.jonny.n0t3s.addInfo.UI.addInfo.toastMessage;

public class addInfoReposatoryImp extends ContextWrapper implements addInfoResposatory {


    public static Context cont;


    FirebaseUser mainUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore myCollection;



    SharedPreferences mSharedPref;
    User user = new User ();

    String myTime;;
    String likeCount = "0";
    public Map < String, Object > notes  = new HashMap<>();

    public addInfoReposatoryImp(Context base) {
        super(base);

        cont = base;

    }





    @Override
    public void pushNotes( String title, String details, String date,String money, Switch mySwitch) {




        //set User values
        user.setName(mainUser.getDisplayName());
        user.setTitle(title);
        user.setDetails(details);

        user.setYear(date);

        user.settimeStampMe(Utils.timeStampMe());
        myTime = user.gettimeStampMe();
        mainUser = FirebaseAuth.getInstance().getCurrentUser();
        user.setEma(mainUser.getEmail());

        user.setMoneyAmount(money);
        user.setLikeCounter(likeCount);
        user.setUserLike(false);




        //writing to database photo name, photographer, and year taken
        if (!user.getTitle().equals("") && !user.equals("") && !user.getDetails().equals("") &&
                !mySwitch.isChecked() && !user.getMoneyAmount().equals("")) {

            //notes.put("userID",mainUser.getUid());

            notes.put("title", user.getTitle());

            notes.put("name",  user.getName());
            notes.put("details", user.getDetails());
            notes.put("year", user.getYear());
            notes.put("ema", user.getEma());
            notes.put("timeStampMe", user.gettimeStampMe());
            notes.put("likeCounter", user.getLikeCounter());
            notes.put("money", "$"+user.getMoneyAmount());
            notes.put("userLike", user.getUserLike());

            mSharedPref =
                    cont.getSharedPreferences(cont.getPackageName(), Activity.MODE_PRIVATE);



            user.setUserID(mainUser.getUid());

            FirebaseMessaging.getInstance().subscribeToTopic("/topics/userID").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                  
                }
            });
            pushNoteData(user.getUserID(), user.gettimeStampMe(),mySwitch);


        }else if(!user.getTitle().equals("") && !user.equals("") &&
                mySwitch.isChecked()&& !user.getDetails().equals("") ){

            //notes.put("userID",mainUser.getUid());

            notes.put("title", user.getTitle());
            notes.put("name",  user.getName());
            notes.put("details", user.getDetails());
            notes.put("year", user.getYear());
            notes.put("ema", user.getEma());
            notes.put("timeStampMe", user.gettimeStampMe());
            notes.put("likeCounter", user.getLikeCounter());
            notes.put("money", "$"+user.getMoneyAmount());
            notes.put("userLike", user.getUserLike());



            user.setUserID(mainUser.getUid());

            FirebaseMessaging.getInstance().subscribeToTopic("/topics/userID").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
            pushNoteData(user.getUserID(), user.gettimeStampMe(),mySwitch);

        }else if(!mySwitch.isChecked() ){
            toastMessage = "Please don't leave any any fields blank when making a public post";
            Utils.toastMessage( toastMessage, cont);

        } else {

            toastMessage = "Please don't leave title, date, and details empty when making a private post";
            Utils.toastMessage( toastMessage, cont);
        // Toast.makeText(this, "Please don't live any fields blank",Toast.LENGTH_LONG).show();
    }

    }

        @Override
        public void pushNoteData(String id, String timeStamp, Switch privateTrue){

            myCollection = FirebaseFirestore.getInstance();

            if (privateTrue.isChecked()) {

                myCollection.collection(id).document(id + timeStamp).set(notes)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pToastMessage = "Special Note Was Saved on Private List";
                                Utils.toastMessage(pToastMessage, cont);
                                ((Activity) cont).finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(cont, "ERROR" + e.toString(),
                                        Toast.LENGTH_SHORT).show();
                                Log.d("TAG", e.toString());

                            }
                        });
            } else {
                myCollection.collection("Notes").document(timeStamp+user.getTitle()).set(notes)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                privateMessage = "Special Note Was Saved On Public List";
                                Utils.toastMessage(privateMessage, cont);
                                ((Activity) cont).finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(cont, "ERROR" + e.toString(),
                                        Toast.LENGTH_SHORT).show();
                                Log.d("TAG", e.toString());

                            }
                        });


            }

        }
}

