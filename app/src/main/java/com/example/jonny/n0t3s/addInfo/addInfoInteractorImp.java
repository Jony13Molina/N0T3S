package com.example.jonny.n0t3s.addInfo;


import android.content.Context;
import android.content.ContextWrapper;
import android.widget.Switch;

public class addInfoInteractorImp extends ContextWrapper implements addInfoInteractor {


    Context context;
    addInfoReposatoryImp addInfoRes;
    public addInfoInteractorImp(Context base) {
        super(base);

        context = base;


        addInfoRes= new addInfoReposatoryImp(context);
    }



    @Override
    public void putNotes( String title, String details, String date, Switch mySwitch) {

        addInfoRes.pushNotes(title, details, date,mySwitch);

    }

    @Override
    public void pushData(String id, String timeStamp, Switch mySwitch) {

        addInfoRes.pushNoteData(id, timeStamp, mySwitch);

    }
}
