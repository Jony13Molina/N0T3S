package com.example.jonny.n0t3s;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class viewPresentImp  extends ContextWrapper implements viewPresenter {

    Context myCont;
    viewInfoView mView;
    viewInteractorImp myInteractor;
    public viewPresentImp(Context base) {
        super(base);
        myCont = base;
        myInteractor = new viewInteractorImp(myCont);
    }

    @Override
    public void startDelete(List<User>userList, User user, RecyclerAdapter adapter, int pos) {
        myInteractor.deleteNote(userList, user, adapter, pos);
    }



    @Override
    public void shareNote(User user) {
        myInteractor.shareNote(user);

    }

    @Override
    public FirebaseFirestore getNoteData() {
        return myInteractor.getMyData();

    }




}
