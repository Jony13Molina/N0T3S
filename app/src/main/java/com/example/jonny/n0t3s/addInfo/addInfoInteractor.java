package com.example.jonny.n0t3s.addInfo;

import android.widget.Switch;

public interface addInfoInteractor {

    void putNotes( String title, String details, String date,String money,  Switch mySwitch );

    void pushData(String id, String timeStamp, Switch mySwitch );

}
