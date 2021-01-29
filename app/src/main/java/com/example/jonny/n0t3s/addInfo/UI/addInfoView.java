package com.example.jonny.n0t3s.addInfo.UI;

import android.widget.Switch;

import com.example.jonny.n0t3s.User;

public interface addInfoView {


    void pushNotes( );



    void pushData(String id, String timeStMe, User myUser,  Switch mySwitch);
}
