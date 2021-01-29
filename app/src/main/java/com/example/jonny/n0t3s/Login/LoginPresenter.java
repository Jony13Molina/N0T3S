package com.example.jonny.n0t3s.Login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;

public interface LoginPresenter {


    GoogleSignInClient handleGoogleClient(Context cont, GoogleSignInClient client);
    void handleGoogleClientRes();

    void handleClientSignRequest(GoogleSignInAccount account, Context cont, ProgressBar myBar);
    void onDestroy();
    void onStart();
    void onCreate();

    void fireLogIn(String email, String password, Context cont, ProgressBar myBar, View myView);
    void registerUser(String email, String password, Context cont);
    void checkConnectivity(Context cont);
    void resetPass(Context cont);

}
