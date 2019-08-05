package com.example.jonny.n0t3s.Main;

import com.example.jonny.n0t3s.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public interface MainPresenter {

    void startDelete(List<User> userList, User user, RecyclerTwoAdapter adapter, int pos);
    void setLike(User user);
    FirebaseFirestore getNoteData();
    void setLikeDatabase(String id, String path, String likeVal, boolean likeState);
}
