package com.example.jonny.n0t3s;


import android.content.Context;
import android.content.ContextWrapper;
import android.widget.Switch;

import java.util.Map;

public class addInfoInteractorImp extends ContextWrapper implements addInfoInteractor{


    Context context;
    addInfoReposatoryImp addInfoRes;
    public addInfoInteractorImp(Context base) {
        super(base);

        context = base;


        addInfoRes= new addInfoReposatoryImp(context);
    }



    @Override
    public void putNotes( User myUser, Switch mySwitch) {

        addInfoRes.pushNotes( myUser, mySwitch);

    }

    @Override
    public void pushData(String id, String timeStamp, Switch mySwitch) {

        addInfoRes.pushNoteData(id, timeStamp, mySwitch);

    }
}
