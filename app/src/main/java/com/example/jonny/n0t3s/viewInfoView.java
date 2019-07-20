package com.example.jonny.n0t3s;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public interface viewInfoView {

    void initDel(  int pos);
    void shareNotes(int pos);
    void getDataNotes();

}
