package com.example.jonny.n0t3s.Login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;

import com.example.jonny.n0t3s.Login.UI.LoginView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginPresenterImpl extends FragmentActivity implements LoginPresenter {

    private LoginView mLoginView;
    private loginInteractor mLoginInteractor;
    Context myContext;
    GoogleSignInClient client;

    public LoginPresenterImpl(LoginView view, Context context){
        mLoginView = view;
        myContext = context;

        mLoginInteractor = new loginInteractorImp();
    }


    @Override
    public GoogleSignInClient handleGoogleClient(Context context, GoogleSignInClient client){
        client =mLoginInteractor.beginGoogleClient(context, client);
        return client;
    }


    @Override
    public void handleGoogleClientRes(){
        mLoginInteractor.loginRes();
    }


    @Override
    public void handleClientSignRequest(GoogleSignInAccount account, Context cont, ProgressBar myBar){
        mLoginInteractor.doSignIn(account, cont, myBar);
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


    @Override
    public void fireLogIn(String email, String password, Context cont, ProgressBar myBar, View myView){
        mLoginInteractor.fireSignIn(email, password, cont, myBar, myView);
    }



    @Override
    public void registerUser(String email, String password, Context cont){
        mLoginInteractor.registerAccount(email, password, cont);
    }

    @Override
    public void checkConnectivity(Context cont){
        mLoginInteractor.checkConnection(cont);
    }
    @Override
    public void resetPass(Context cont){
        mLoginInteractor.resetPass(cont);
    }



}
