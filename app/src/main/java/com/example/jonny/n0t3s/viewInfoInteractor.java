package com.example.jonny.n0t3s;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public interface viewInfoInteractor {


    void deleteNote(List<User>userList,User user, RecyclerAdapter adapter, int pos);
    void shareNote(User user);
    FirebaseFirestore getMyData();

}
