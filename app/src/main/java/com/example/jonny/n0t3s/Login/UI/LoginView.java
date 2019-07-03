package com.example.jonny.n0t3s.Login.UI;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

public interface LoginView {



    void loginRequest();

    void googleClient();

    void ActResult(int rqCode, int rsCode, Intent data);


    void handleLogIn(GoogleSignInAccount account);

    void mainAccess();

    void loginError(String error);


}
