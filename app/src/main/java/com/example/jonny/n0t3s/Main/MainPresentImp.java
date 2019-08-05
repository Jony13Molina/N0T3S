package com.example.jonny.n0t3s.Main;

import android.content.Context;
import android.content.ContextWrapper;

import com.example.jonny.n0t3s.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainPresentImp extends ContextWrapper implements MainPresenter {

    MainInteractorImp myInteractor;
    Context myCont;
    public MainPresentImp(Context base) {
        super(base);

        myCont = base;
        myInteractor = new MainInteractorImp(myCont);

    }

    @Override
    public void startDelete(List<User> userList, User user, RecyclerTwoAdapter adapter, int pos) {
        myInteractor.deleteNote(userList,user, adapter, pos);
    }

    @Override
    public void setLike(User user) {
        myInteractor.setLike(user);

    }

    @Override
    public FirebaseFirestore getNoteData() {
        return myInteractor.getMyData();
    }

    @Override
    public void setLikeDatabase(String id, String path, String likeVal, boolean likeState) {
        myInteractor.setLikeData(id, path, likeVal, likeState);
    }
}
