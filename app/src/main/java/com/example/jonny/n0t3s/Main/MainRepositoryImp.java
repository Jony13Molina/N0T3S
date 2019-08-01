package com.example.jonny.n0t3s.Main;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jonny.n0t3s.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainRepositoryImp extends ContextWrapper implements MainRepository {
    Context myCont;
    FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
    String userPath;
    String pathId;
    FirebaseFirestore myCollection =  FirebaseFirestore.getInstance();
    int likeCount;
    User myUser;
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
    public void setLike(User user) {


        user.setUserID(fireUser.getUid());


        //make sure the person who made the note or post
        //doesn't like their own note/post
        //then check for the click and add/subtract accordingly
        if (user.getUserID().equals(fireUser.getUid())) {



            if (click) {
                likeCount++;
                String countVal = Integer.toString(likeCount);
                user.setLikeCounter(countVal);

                fireUser = FirebaseAuth.getInstance().getCurrentUser();

                //user.setUserID(fireUser.getUid());
                userPath = user.gettimeStampMe();
                Log.d("timestamo!!!!!!!!!!!!", user.gettimeStampMe());
                pathId = "Notes";


                click = false;
                user.setUserLike(click);
                updateLike(pathId, userPath, countVal, user.getUserLike());
                //user.setUserLike(false);

            } else {
                likeCount--;
                String countVal = Integer.toString(likeCount);
                user.setLikeCounter(countVal);

                fireUser = FirebaseAuth.getInstance().getCurrentUser();

                //user.setUserID(fireUser.getUid());
                userPath = user.gettimeStampMe();
                pathId = "Notes";



                //user.setUserLike(true);
                click = true;
                user.setUserLike(click);
                updateLike(pathId, userPath, countVal,user.getUserLike());
            }
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
}
