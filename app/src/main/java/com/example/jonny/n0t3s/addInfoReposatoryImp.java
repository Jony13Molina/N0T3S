package com.example.jonny.n0t3s;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.jonny.n0t3s.addInfo.pToastMessage;
import static com.example.jonny.n0t3s.addInfo.privateMessage;
import static com.example.jonny.n0t3s.addInfo.toastMessage;

public class addInfoReposatoryImp extends ContextWrapper implements addInfoResposatory {


    public static Context cont;


    FirebaseUser mainUser;
    FirebaseFirestore myCollection;

    public Map < String, Object > notes;

    public addInfoReposatoryImp(Context base) {
        super(base);

        cont = base;

    }

    @Override
    public void pushNotes( User user, Switch mySwitch) {


        mainUser = FirebaseAuth.getInstance().getCurrentUser();


        //writing to database photo name, photographer, and year taken
        if (!user.getTitle().equals("") && !user.equals("")) {
            notes = new HashMap<>();
            //notes.put("userID",mainUser.getUid());

            notes.put("title", user.getTitle());

            notes.put("details", user.getDetails());
            notes.put("year", user.getYear());
            notes.put("ema", user.getEma());
            notes.put("timeStampMe", user.gettimeStampMe());
            notes.put("likeCounter", user.getLikeCounter());
            notes.put("userLike", user.getUserLike());


            user.setUserID(mainUser.getUid());
            pushNoteData(user.getUserID(), user.gettimeStampMe(),mySwitch);

        }else{
        toastMessage = "Please don't leave any fileds blank";
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
                                //Toast.makeText(addInfo.this, "Special Note Saved on Private List!", Toast.LENGTH_LONG).show();
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
                myCollection.collection("Notes").document(timeStamp).set(notes)
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

