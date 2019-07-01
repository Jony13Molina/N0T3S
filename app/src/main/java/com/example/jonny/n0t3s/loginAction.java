package com.example.jonny.n0t3s;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;

public interface loginAction {



    GoogleApiClient googleLogIn(Context context);

    void startLogin();

    void signInReq(int rqCode, int rsCode, Intent signInData, Context cont);

    void carrySignIn(GoogleSignInAccount account, Context context);

    void authUser();


}
