package com.example.jonny.n0t3s.Login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;

public interface loginAction {



    GoogleSignInClient googleLogIn(Context context);

    void startLogin();


    void carrySignIn(GoogleSignInAccount account, Context context, ProgressBar myBar);

    void authUser();
    void fireSignMe(String email, String password, Context cont, ProgressBar myBar, View mView);
    void registerAccount(String email, String password, Context cont);
    boolean checkConnection(Context cont);

    void resetPass(Context cont);


}
