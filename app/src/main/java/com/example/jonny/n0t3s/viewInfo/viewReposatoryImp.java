package com.example.jonny.n0t3s.viewInfo;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.jonny.n0t3s.User;
import com.example.jonny.n0t3s.Utils;
import com.example.jonny.n0t3s.viewInfo.RecyclerAdapter;
import com.example.jonny.n0t3s.viewInfo.viewInfo;
import com.example.jonny.n0t3s.viewInfo.viewPresentImp;
import com.example.jonny.n0t3s.viewInfo.viewPresenter;
import com.example.jonny.n0t3s.viewInfo.viewReposatory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.jonny.n0t3s.addInfo.addInfoReposatoryImp.cont;

public class viewReposatoryImp extends ContextWrapper implements viewReposatory {

    FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
    User user;
    Context myContext;
    String userPath;

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
    public void shareThisNote(User user) {

        Map<String, Object> notes = new HashMap<>();
        //notes.put("userID",mainUser.getUid());

        notes.put("title", user.getTitle());
        notes.put("details", user.getDetails());
        notes.put("year", user.getYear());
        notes.put("ema", user.getEma());
        notes.put("timeStampMe", user.gettimeStampMe());
        notes.put("likeCounter", user.getLikeCounter());
        notes.put("userLike", user.getUserLike());


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