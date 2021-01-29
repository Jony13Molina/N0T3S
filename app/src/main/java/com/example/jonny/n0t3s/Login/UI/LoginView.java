package com.example.jonny.n0t3s.Login.UI;

import android.content.Intent;

import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface LoginView {



    void loginRequest();

    void googleClient();



    void handleLogIn(GoogleSignInAccount account);

    void mainAccess();

    void loginError(String error);

    void registerUser();

    void fireLogin(ProgressBar myBar, View myView);

    void checkConnection();

    void resetPassword();

}
