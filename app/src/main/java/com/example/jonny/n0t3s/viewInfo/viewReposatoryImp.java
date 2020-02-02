package com.example.jonny.n0t3s.viewInfo;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import android.util.Log;

import com.example.jonny.n0t3s.User;
import com.example.jonny.n0t3s.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class viewReposatoryImp extends ContextWrapper implements viewReposatory {

    FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
    User user;
    public static Context myContext;
    String userPath;

    String myToken;
    SharedPreferences mSharedPref;
    List<User> myUserList;
    List<User> userList;
    RecyclerAdapter myAdapter;
    FirebaseFirestore myCollection = FirebaseFirestore.getInstance();
    viewInfo myView;

    public viewReposatoryImp(Context base) {
        super(base);
        myContext = base;
        myView = new viewInfo();

    }

    public void setToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) myContext,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                myToken = instanceIdResult.getToken();
                Log.e("Token",myToken);

                mSharedPref =
                        myContext.getSharedPreferences(myContext.getPackageName(), Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("token",myToken);
                editor.commit();
            }
        });
    }

    //delete note at position
    @Override
    public void deleteNoteAt(List<User> userNotes, User myUser, RecyclerAdapter adapter, int pos) {

        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        myUser.setUserID(fireUser.getUid());
        userPath = myUser.getUserID() + myUser.gettimeStampMe();
        itemDelete(userNotes, myUser.getUserID(), userPath, pos, adapter);

        Log.d("This is the path!", userPath);
    }

    //share notes
    @Override
    public void shareThisNote(User user,String moneyInput) {

        Map<String, Object> notes = new HashMap<>();
        //notes.put("userID",mainUser.getUid());


        setToken();
        Log.e("money amount 1000000", moneyInput);

        notes.put("token", user.getUserToken());
        user.setUserLike(false);
        notes.put("title", user.getTitle());
        notes.put("details", user.getDetails());
        notes.put("year", user.getYear());
        notes.put("ema", user.getEma());
        notes.put("timeStampMe", user.gettimeStampMe());
        notes.put("likeCounter", user.getLikeCounter());
        notes.put("money",  moneyInput);
        notes.put("userLike", user.getUserLike());



        mSharedPref =
                myContext.getSharedPreferences(myContext.getPackageName(), Activity.MODE_PRIVATE);
        String toekn = mSharedPref.getString("token", myToken);
        notes.put("token", toekn);

        myCollection.collection("Notes").document(user.gettimeStampMe()).set(notes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Utils.toastMessage("Special Note Shared", myContext);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Utils.toastMessage("Error!!!" + e.toString(), myContext);

                    }
                });


    }

    //delete notes
    @Override
    public void itemDelete(final List<User> userNotes, String id, String path,
                           final int pos, final RecyclerAdapter adapter) {

        myCollection = FirebaseFirestore.getInstance();

        myCollection.collection(id).document(path).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                      /*  userNotes.remove(pos);
                        adapter.notifyItemRemoved(pos);
                        adapter.notifyItemRangeChanged(pos, userNotes.size());
                        Utils.toastMessage("Note was deleted", myContext);*/

//                        myPresenter.deleteItems(userNotes, pos, adapter, myContext);
                        myView.itemDelete(userNotes, pos, adapter, myContext);
                    }
                });

    }


/*-----------------------------------------------------------------------------------------------/
/   This marks the end of methods related to adapter logic/action.                               /
/  This ,method is to get the data from fireStore and populate our view.                         /
/   This is a mehtod to get the user data                                                        /
/-----------------------------------------------------------------------------------------------*/

    public FirebaseFirestore getData() {


        myCollection = FirebaseFirestore.getInstance();


        return myCollection;



    }







}
