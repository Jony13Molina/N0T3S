package com.example.jonny.n0t3s.Login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;

public class loginInteractorImp implements loginInteractor {

    private loginAction myLoginAction;
    private Context context;





    public loginInteractorImp(){
        myLoginAction = new loginActionImp();
        //myLoginAction = new loginActionImp(context.getApplicationContext());

    }



    @Override
    public GoogleSignInClient beginGoogleClient(Context cont, GoogleSignInClient client){
       client =  myLoginAction.googleLogIn(cont);

       return client;

    }



    @Override
    public void loginRes(){
        myLoginAction.startLogin();
    }


    @Override
    public void doSignIn(GoogleSignInAccount account, Context cont,ProgressBar myBar) {
        myLoginAction.carrySignIn(account, cont, myBar);

    }

    @Override
    public void getCurrUser(){
        myLoginAction.authUser();
    }
    @Override
    public void fireSignIn(String email, String password, Context  cont, ProgressBar myBar, View mView){
        myLoginAction.fireSignMe(email, password, cont,myBar, mView);
    }
    @Override
    public void registerAccount(String email, String password, Context cont){
        myLoginAction.registerAccount(email, password, cont);
    }
    @Override
    public void checkConnection(Context cont){
        myLoginAction.checkConnection(cont);
    }
    @Override
    public void resetPass(Context cont){
        myLoginAction.resetPass(cont);
    }
}
