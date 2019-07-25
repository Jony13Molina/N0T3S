package com.example.jonny.n0t3s.viewInfo;

import android.content.Context;
import android.content.ContextWrapper;

import com.example.jonny.n0t3s.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class viewInteractorImp extends ContextWrapper implements viewInfoInteractor {
    Context myContext;
    viewReposatoryImp myReposatory;
    viewPresentImp myPresenter;
    public viewInteractorImp(Context base) {
        super(base);

        myContext = base;

        myReposatory = new viewReposatoryImp(myContext);

    }

    @Override
    public void deleteNote(List<User> userList, User user, RecyclerAdapter adapter, int pos) {

        myReposatory.deleteNoteAt(userList, user, adapter, pos);

    }

    @Override
    public void shareNote(User user) {
        myReposatory.shareThisNote(user);

    }



    @Override
    public FirebaseFirestore getMyData(){
        return myReposatory.getData();

    }




}
