package com.example.jonny.n0t3s.Login;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

public class loginInteractorImp implements loginInteractor {

    private loginAction myLoginAction;
    private Context context;





    public loginInteractorImp(){
        myLoginAction = new loginActionImp();
        //myLoginAction = new loginActionImp(context.getApplicationContext());

    }



    @Override
    public GoogleApiClient beginGoogleClient(Context cont, GoogleApiClient client){
       client =  myLoginAction.googleLogIn(cont);

       return client;

    }



    @Override
    public void loginRes(){
        myLoginAction.startLogin();
    }
    @Override
    public void actRes(int rqCode, int rsCode, Intent data, Context cont) {
        myLoginAction.signInReq(rqCode, rsCode, data, cont);

    }

    @Override
    public void doSignIn(GoogleSignInAccount account, Context cont) {
        myLoginAction.carrySignIn(account, cont);

    }

    @Override
    public void getCurrUser(){
        myLoginAction.authUser();
    }
}
