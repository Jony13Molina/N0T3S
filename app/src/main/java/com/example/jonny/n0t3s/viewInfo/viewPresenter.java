package com.example.jonny.n0t3s.viewInfo;

import android.content.Context;

import com.example.jonny.n0t3s.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public interface viewPresenter {
    void startDelete(List<User>userList, User user, RecyclerAdapter adapter, int pos);
    void shareNote(User user,String infoAMount);
    FirebaseFirestore getNoteData();





}
