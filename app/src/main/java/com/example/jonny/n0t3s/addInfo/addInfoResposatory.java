package com.example.jonny.n0t3s.addInfo;

import android.widget.Switch;



public interface addInfoResposatory {

    void pushNotes( String title, String details,String money ,String date,Switch mySwitch);

    void pushNoteData(String id, String timeStamp, Switch mySwitch);
}
