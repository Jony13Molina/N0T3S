package com.example.jonny.n0t3s;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jonny.n0t3s.Login.UI.LoginActivity;
import com.example.jonny.n0t3s.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUp extends AppCompatActivity implements View.OnClickListener {


    TextView passwdField, emailField;
    FirebaseAuth myAuthUser = FirebaseAuth.getInstance();
    Button registerMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        passwdField = findViewById(R.id.passwordtext);
        emailField = findViewById(R.id.emailText);
        registerMe = findViewById(R.id.registerMe);

        registerMe.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {


        registerUser();


    }


    public void registerUser(){


        final String email = emailField.getText().toString().trim();
        String password= passwdField.getText().toString().trim();
        if(checkConnectionAndInput(email,password,this)==false)
        {
            return;
        }
        // Creating a new user
        if(email.contains("@gmail.com")) {

            Utils.toastMessage("Use Google Sign In", this);
        }else {


            myAuthUser.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //if task is unsuccesfull check for a valid email and if an account exist with that email
                    if (!task.isSuccessful())
                    {
                        try
                        {
                            throw task.getException();
                        }

                        // if user enters wrong password.
                        catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                        {

                            Utils.toastMessage("Please enter a valid email", getApplicationContext());
                        }
                        catch (FirebaseAuthUserCollisionException existEmail)
                        {
                            Utils.toastMessage("There exists an account with this email already", getApplicationContext());


                        }
                        catch (Exception e)
                        {


                        }
                    //if the task is succesfull and user has entered a valid email and there
                    //no existing account with that email, then create account
                    }else if(task.isSuccessful()){

                        FirebaseUser user = myAuthUser.getCurrentUser();


                        String name = email.split("@", 2)[0];

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();

                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                    }





                    Utils.toastMessage("Account Created",getApplicationContext());
                        Intent next_activity = null;

                        //once account has been created transition bck to the login page, to
                        //sign in with new credentials
                        next_activity = new Intent(SignUp.this, LoginActivity.class);

                        startActivity(next_activity);
                    }





            });



        }

    }

    // Checks0 both input and connection and returns a boolean
    public boolean checkConnectionAndInput(String email, String password, Context cont)
    {
        // Checks if fields are empty
        if(TextUtils.isEmpty(email))
        {
            Utils.toastMessage("Please enter a valid email",cont);
            return false;
        }
        if(TextUtils.isEmpty(password))
        {
            Utils.toastMessage("Please enter a valid password", cont);
            return false;
        }

        if(email.contains("@gmail.com")){
            Utils.toastMessage("Enter a non Gmail Account or Use Google Sign In", cont);
        }
        // Check if there is internet connection
        return checkConnection(cont);
    }


    public boolean checkConnection(Context cont){

        ConnectivityManager myConnection = (ConnectivityManager)cont.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = myConnection.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(!isConnected){
            Utils.toastMessage("Check Internet Connection", this);
            return false;
        }
        else
        {
            return true;
        }

    }





}