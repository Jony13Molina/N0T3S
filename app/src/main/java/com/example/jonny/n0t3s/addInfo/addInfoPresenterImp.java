package com.example.jonny.n0t3s.addInfo;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.Switch;

public class addInfoPresenterImp extends ContextWrapper implements addInfoPresenter {

    Context cont;
    private addInfoInteractorImp myInteractor;
    public addInfoPresenterImp(Context base) {
        super(base);
        cont = base;
        myInteractor = new addInfoInteractorImp(cont);
    }

    @Override
    public void pushNotes( String title, String details, String date,String money,  Switch mySwitch) {
        myInteractor.putNotes(title, details, date, money,mySwitch);

    }

    @Override
    public void pushData(String id, String timeStamp, Switch mySwitch) {
        myInteractor.pushData(id,timeStamp, mySwitch);

    }
}
