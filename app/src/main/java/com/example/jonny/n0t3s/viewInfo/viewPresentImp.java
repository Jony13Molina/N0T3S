package com.example.jonny.n0t3s.viewInfo;

import android.content.Context;
import android.content.ContextWrapper;

import com.example.jonny.n0t3s.RecyclerTwoAdapter;
import com.example.jonny.n0t3s.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class viewPresentImp  extends ContextWrapper implements viewPresenter {

    Context myCont;
    viewInfo mView;
    viewInteractorImp myInteractor;
    public viewPresentImp(Context base) {
        super(base);
        myCont = base;
        myInteractor = new viewInteractorImp(myCont);
        mView = new viewInfo();
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
