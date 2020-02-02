package com.example.jonny.n0t3s.Login;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

public interface LoginPresenter {


    GoogleApiClient handleGoogleClient(Context cont, GoogleApiClient client);
    void handleGoogleClientRes();
    void handleActResult(int rqCode, int rsCode, Intent data,Context cont);
    void handleClientSignRequest(GoogleSignInAccount account, Context cont);
    void onDestroy();
    void onStart();
    void onCreate();

    void fireLogIn(String email, String password,Context cont);
    void registerUser(String email, String password, Context cont);
    void checkConnectivity(Context cont);
    void resetPass(Context cont);

}
