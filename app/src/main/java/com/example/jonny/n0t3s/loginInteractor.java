package com.example.jonny.n0t3s;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

public interface loginInteractor {

    GoogleApiClient beginGoogleClient(Context cont, GoogleApiClient client);
    void loginRes();
    void actRes(int rqCode, int rsCode, Intent data, Context cont);
    void doSignIn(GoogleSignInAccount account, Context cont);
    void getCurrUser();


}
