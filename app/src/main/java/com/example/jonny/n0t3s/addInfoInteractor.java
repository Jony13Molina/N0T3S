package com.example.jonny.n0t3s;

import android.widget.Switch;

import java.util.Map;

public interface addInfoInteractor {

    void putNotes( User myUser, Switch mySwitch );

    void pushData(String id, String timeStamp, Switch mySwitch );

}
