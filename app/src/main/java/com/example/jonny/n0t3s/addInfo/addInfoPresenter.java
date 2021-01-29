package com.example.jonny.n0t3s.addInfo;

import android.widget.Switch;

public interface addInfoPresenter {


    void pushNotes(String title, String details,String money, String date, Switch mySwitch);

    void pushData(String id, String timeStamp, Switch mySwitch);
}
