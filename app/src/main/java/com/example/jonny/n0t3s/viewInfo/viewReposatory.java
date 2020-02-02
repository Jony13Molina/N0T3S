package com.example.jonny.n0t3s.viewInfo;

import android.content.Context;

import com.example.jonny.n0t3s.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public interface viewReposatory {

    void deleteNoteAt(List<User> userNotes, User user, RecyclerAdapter adapter, int pos);

    void shareThisNote(User user,String moneyInput);

    void itemDelete(List<User> userNotes, String id, String path, int pos, RecyclerAdapter adapter);

    FirebaseFirestore getData();




}