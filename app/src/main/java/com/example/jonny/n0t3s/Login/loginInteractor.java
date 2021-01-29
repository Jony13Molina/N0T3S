package com.example.jonny.n0t3s.Login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;

public interface loginInteractor {

    GoogleSignInClient beginGoogleClient(Context cont, GoogleSignInClient client);
    void loginRes();
    void doSignIn(GoogleSignInAccount account, Context cont, ProgressBar myBar);
    void getCurrUser();
    void fireSignIn(String email, String password, Context cont, ProgressBar myBar, View myView);
    void registerAccount(String email, String password, Context cont);
    void checkConnection(Context cont);
    void resetPass(Context cont);


}
