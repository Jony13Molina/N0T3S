package com.example.jonny.n0t3s;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity
        implements LoginView, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    SignInButton signIn;
    LoginPresenterImpl myPresenter;
    loginActionImp myaction;
    GoogleApiClient myClient, getMyClient;
    public Context cont = getApplication();
    private static final int RC_SIGN_IN = 9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        myPresenter = new LoginPresenterImpl(this, this);
        myClient= myPresenter.handleGoogleClient(getApplication(), myClient);

        signIn = (SignInButton) findViewById(R.id.sign_in_button);
        signIn.setOnClickListener(this);

    }

    @Override
    protected void onStart(){
        super.onStart();
        myPresenter.onStart();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        myPresenter.onDestroy();
    }




    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sign_in_button:
                mainAccess();
                break;
        }








    }

    @Override
    public void googleClient(){
        myPresenter.handleGoogleClient(cont, myClient);
    }


    @Override
    public void loginRequest(){
       // getMyClient = myaction.googleLogIn(this);
        Intent logInIntent = Auth.GoogleSignInApi.getSignInIntent(myClient);
        startActivityForResult(logInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ActResult(requestCode, resultCode, data);
    }


    @Override
    public void handleLogIn(GoogleSignInAccount account) {

        myPresenter.handleClientSignRequest(account, getApplication());
    }

    @Override
    public void ActResult(int rqCode, int rsCode, Intent data){
        myPresenter.handleActResult(rqCode,rsCode, data,getApplication());
    }

    @Override
    public void mainAccess() {

        loginRequest();
    }

    @Override
    public void loginError(String error) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}