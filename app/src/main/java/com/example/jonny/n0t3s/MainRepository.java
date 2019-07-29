package com.example.jonny.n0t3s;

import com.example.jonny.n0t3s.viewInfo.RecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public interface MainRepository {

    void deleteNote(List<User> userList, User user, RecyclerTwoAdapter adapter, int pos);
    void setLike(User user);
    void updateLike(String id, String path, String likeVal);
    FirebaseFirestore getMyData();
}
