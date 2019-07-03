package com.example.jonny.n0t3s.Login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.example.jonny.n0t3s.Login.UI.LoginView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginPresenterImpl extends FragmentActivity implements LoginPresenter {

    private LoginView mLoginView;
    private loginInteractor mLoginInteractor;
    Context myContext;
    GoogleApiClient client;

    public LoginPresenterImpl(LoginView view, Context context){
        mLoginView = view;
        myContext = context;

        mLoginInteractor = new loginInteractorImp();
    }


    @Override
    public GoogleApiClient handleGoogleClient(Context context, GoogleApiClient client){
        client =mLoginInteractor.beginGoogleClient(context, client);
        return client;
    }


    @Override
    public void handleGoogleClientRes(){
        mLoginInteractor.loginRes();
    }

    @Override
    public void handleActResult(int rqCode, int rsCode,Intent data, Context cont){
        mLoginInteractor.actRes(rqCode,rsCode, data, cont);
    }
    @Override
    public void handleClientSignRequest(GoogleSignInAccount account, Context cont){
        mLoginInteractor.doSignIn(account, cont);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy(){

        mLoginView = null;

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart(){




        mLoginInteractor.getCurrUser();


    }

    @Override
    public void onCreate(){


    }






}
