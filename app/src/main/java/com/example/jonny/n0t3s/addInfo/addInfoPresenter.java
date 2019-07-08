package com.example.jonny.n0t3s.addInfo;

import android.widget.Switch;

import java.util.Map;

public interface addInfoPresenter {


    void pushNotes(String title, String details, String date, Switch mySwitch);

    void pushData(String id, String timeStamp, Switch mySwitch);
}
