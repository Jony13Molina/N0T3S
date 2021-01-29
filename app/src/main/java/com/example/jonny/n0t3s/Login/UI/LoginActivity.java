package com.example.jonny.n0t3s.Login.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jonny.n0t3s.Login.LoginPresenterImpl;
import com.example.jonny.n0t3s.Login.loginActionImp;
import com.example.jonny.n0t3s.Main.MainActivity;
import com.example.jonny.n0t3s.R;
//import com.example.jonny.n0t3s.SignUp;
import com.example.jonny.n0t3s.SignUp;
import com.example.jonny.n0t3s.User;
import com.example.jonny.n0t3s.Utils;
import com.example.jonny.n0t3s.jobsCompleted.jobsCompleted;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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
    EditText usernameText;
    EditText passwordText;
    Button fireSignIn, registerMe;


    private ProgressBar pgsBar;
    TextView forgotPassword;

    Fragment fragment;

    public LoginPresenterImpl myPresenter;
    loginActionImp myaction;
    GoogleSignInClient myClient, getMyClient;
    public Context cont = getApplication();
    private static final int RC_SIGN_IN = 9001;
    FirebaseAuth myAuthUser = FirebaseAuth.getInstance();


    GoogleSignInAccount myAccount;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        myPresenter = new LoginPresenterImpl(this, this);
        myClient= myPresenter.handleGoogleClient(getApplication(), myClient);


        pgsBar = (ProgressBar) findViewById(R.id.pBar);


        signIn = (SignInButton) findViewById(R.id.sign_in_button);

        fireSignIn = (Button)findViewById(R.id.fireLogin);
        usernameText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText)findViewById(R.id.passwordtext);

        registerMe = (Button)findViewById(R.id.createUser);
        forgotPassword = (TextView)findViewById(R.id.forgotPassWord);

        //setting click listeners for button actions one is for google sign in and the other
        //regular sign in
        fireSignIn.setOnClickListener(this);
        signIn.setOnClickListener(this);


        //click listener for registering
        registerMe.setOnClickListener(this);
        //clicklistener if user forgot their password
        forgotPassword.setOnClickListener(this);



       // GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);







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

        //listen for actions such as sign in request/ account creation request and carry out
        //the action accordingly
        Intent next_activity = null;
        switch(v.getId()){
            case R.id.sign_in_button:
                mainAccess();

                break;

            case R.id.fireLogin:
                fireLogin(pgsBar, v);

                break;


            case R.id.createUser:
                next_activity = new Intent(LoginActivity.this, SignUp.class);

                startActivity(next_activity);
                break;
               //registerUser();



            case R.id.forgotPassWord:
                resetPassword();
        }








    }

    @Override
    public void googleClient(){
        myPresenter.handleGoogleClient(cont, myClient);
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        //doSomeOperations();

                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            handleLogIn(account);
                        } catch (ApiException e) {
                            // Google Sign In failed, update UI appropriately
                           // Log.w(TAG, "Google sign in failed", e);
                        }

                    }
                }
            });


    @Override
    public void loginRequest(){



        Intent logInIntent = myClient.getSignInIntent();

        someActivityResultLauncher.launch(logInIntent);




    }



    //get activtiy result for sign in request/ goofle sign in
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    //handle google login, get user account
    @Override
    public void handleLogIn(GoogleSignInAccount account) {

        myPresenter.handleClientSignRequest(account, getApplication(), pgsBar);
    }


    //on a succesfull activity result the user is authentiated and we can proceed to the main activity
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


    //methods  firelogin authentication
    @Override
    public void fireLogin(ProgressBar myBar, View mView){
        String email = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();



        myPresenter.fireLogIn(email, password, getApplication(),myBar, mView);
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
//
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