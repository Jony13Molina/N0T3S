package com.example.jonny.n0t3s.Main;

import android.content.Context;
import android.content.ContextWrapper;

import com.example.jonny.n0t3s.Notification;
import com.example.jonny.n0t3s.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainInteractorImp extends ContextWrapper implements MainInteractor {


    Context myContext;
    MainRepositoryImp myRepo;
    public MainInteractorImp(Context base) {
        super(base);
        myContext = base;
        myRepo = new MainRepositoryImp(myContext);

    }

    @Override
    public void deleteNote(List<User> userList, User user, RecyclerTwoAdapter adapter, int pos) {
        myRepo.deleteNote(userList, user, adapter, pos);

    }

    @Override
    public void setLike(User user) {
        myRepo.setLike(user);

    }



    @Override
    public FirebaseFirestore getMyData() {
        return myRepo.getMyData();
    }

    @Override
    public void setLikeData(String id, String path, String likeVal, boolean likeState) {
        myRepo.updateLike(id, path, likeVal, likeState);
    }

    @Override
    public void setNotification(Notification noti, User user) {
        myRepo.storeNotification(noti, user );
    }
}
