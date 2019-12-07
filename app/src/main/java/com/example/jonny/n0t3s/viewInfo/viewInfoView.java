package com.example.jonny.n0t3s.viewInfo;

import android.content.Context;

import com.example.jonny.n0t3s.User;

import java.util.List;

public interface viewInfoView {

    void initDel(  int pos);
    void shareNotes(int pos);
    void getDataNotes();
    void itemDelete(List<User>userNotes, int pos, RecyclerAdapter adapter, Context cont);

}
