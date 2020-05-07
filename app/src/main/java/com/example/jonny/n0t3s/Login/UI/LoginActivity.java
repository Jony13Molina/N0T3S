package com.example.jonny.n0t3s.Login.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jonny.n0t3s.Login.LoginPresenterImpl;
import com.example.jonny.n0t3s.Login.loginActionImp;
import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity
        implements LoginView, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    SignInButton signIn;
    EditText usernameText;
    EditText passwordText;
    Button fireSignIn;
    TextView registerMe;
    TextView forgotPassword;


    public LoginPresenterImpl myPresenter;
    loginActionImp myaction;
    GoogleApiClient myClient, getMyClient;
    public Context cont = getApplication();
    private static final int RC_SIGN_IN = 9001;
    FirebaseAuth myAuthUser = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        myPresenter = new LoginPresenterImpl(this, this);
        myClient= myPresenter.handleGoogleClient(getApplication(), myClient);


        signIn = (SignInButton) findViewById(R.id.sign_in_button);

        fireSignIn = (Button)findViewById(R.id.fireLogin);
        usernameText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText)findViewById(R.id.passwordtext);

        registerMe = (TextView)findViewById(R.id.signInoR);
        forgotPassword = (TextView)findViewById(R.id.forgotPassWord);

        //setting click listeners for button actions one is for google sign in and the other
        //regular sign in
        fireSignIn.setOnClickListener(this);
        signIn.setOnClickListener(this);


        //click listener for registering
        registerMe.setOnClickListener(this);
        //clicklistener if user forgot their password
        forgotPassword.setOnClickListener(this);


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
    public void onResume(){
        super.onResume();
        passwordText.setText("");
        usernameText.setText("");

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sign_in_button:
                mainAccess();
                break;

            case R.id.fireLogin:
                fireLogin();
                break;


            case R.id.signInoR:
                registerUser();
                break;

            case R.id.forgotPassWord:
                resetPassword();
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
        super.onActivityResult(requestCode, resultCode, data);
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


    /*Handling the Firebase Login with other email settings*/


    @Override
    public void fireLogin(){
        String email = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();



        myPresenter.fireLogIn(email, password, getApplication());
    }

    @Override
    public void registerUser(){

        String email = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        myPresenter.registerUser(email,password, getApplicationContext());

    }


   @Override
    public void checkConnection(){
        myPresenter.checkConnectivity(getApplicationContext());

   }

   @Override
    public void resetPassword(){

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);

       final EditText inputEmail = new EditText(this);
       inputEmail.setHint("example@example.com");
       inputEmail.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
       alertDialogBuilder.setView(inputEmail);

       alertDialogBuilder.setTitle("Send Password Reset Email");
       alertDialogBuilder.setPositiveButton("Send",
               new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface arg0, int arg1)
                   {
                       String userEmail = inputEmail.getText().toString().trim();

                       // Send emails to user
                       myAuthUser= FirebaseAuth.getInstance();
                       myAuthUser.sendPasswordResetEmail(userEmail)
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()) {
                                           System.out.println("Email Sent");
                                           Utils.toastMessage("Reset Link Was Sent to The Email Associated With The Account",LoginActivity.this);
                                       }else{
                                           Utils.toastMessage("No Account With That Email", LoginActivity.this);
                                           System.out.println("You failed");
                                       }
                                   }
                               });
                   }
               });

       // Cancels button and alert
       alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
       {
           @Override
           public void onClick(DialogInterface arg0, int arg1) {}
       });

       AlertDialog alertDialog = alertDialogBuilder.create();
       alertDialog.show();


   }



}