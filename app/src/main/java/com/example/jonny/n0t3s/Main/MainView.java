package com.example.jonny.n0t3s.Main;

import android.content.Context;

import com.example.jonny.n0t3s.User;

import java.util.List;

public interface MainView {

    void initDel(  int pos);
    void setLike(int pos);
    void getDataNotes();
    void itemDelete(int pos);
    void dataListner();
    void deleteNote(List<User>userNotes, int pos, RecyclerTwoAdapter adapter, Context cont);
}
