package com.example.jonny.n0t3s;

import android.widget.Switch;



public interface addInfoResposatory {

    void pushNotes( User myUser, Switch mySwitch);

    void pushNoteData(String id, String timeStamp, Switch mySwitch);
}
