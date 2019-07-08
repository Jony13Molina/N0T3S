package com.example.jonny.n0t3s.addInfo;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.Switch;

import com.example.jonny.n0t3s.addInfo.addInfoInteractorImp;
import com.example.jonny.n0t3s.addInfo.addInfoPresenter;

public class addInfoPresenterImp extends ContextWrapper implements addInfoPresenter {

    Context cont;
    private addInfoInteractorImp myInteractor;
    public addInfoPresenterImp(Context base) {
        super(base);
        cont = base;
        myInteractor = new addInfoInteractorImp(cont);
    }

    @Override
    public void pushNotes( String title, String details, String date,  Switch mySwitch) {
        myInteractor.putNotes(title, details, date,  mySwitch);

    }

    @Override
    public void pushData(String id, String timeStamp, Switch mySwitch) {
        myInteractor.pushData(id,timeStamp, mySwitch);

    }
}
