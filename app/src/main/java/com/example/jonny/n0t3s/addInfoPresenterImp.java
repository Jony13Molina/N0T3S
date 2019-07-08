package com.example.jonny.n0t3s;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.Switch;

import com.example.jonny.n0t3s.Login.loginInteractor;

import java.util.Map;

public class addInfoPresenterImp extends ContextWrapper implements addInfoPresenter {

    Context cont;
    private addInfoInteractorImp myInteractor;
    public addInfoPresenterImp(Context base) {
        super(base);
        cont = base;
        myInteractor = new addInfoInteractorImp(cont);
    }

    @Override
    public void pushNotes( User myUser, Switch mySwitch) {
        myInteractor.putNotes(myUser, mySwitch);

    }

    @Override
    public void pushData(String id, String timeStamp, Switch mySwitch) {
        myInteractor.pushData(id,timeStamp, mySwitch);

    }
}
