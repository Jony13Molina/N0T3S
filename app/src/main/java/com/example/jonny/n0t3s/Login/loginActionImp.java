package com.example.jonny.n0t3s.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jonny.n0t3s.Main.MainActivity;
import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.SignUp;
import com.example.jonny.n0t3s.User;
import com.example.jonny.n0t3s.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.jonny.n0t3s.addInfo.UI.addInfo.pToastMessage;

public class loginActionImp extends FragmentActivity implements loginAction {

    FirebaseAuth myAuthUser;
    FirebaseUser currUser;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private static GoogleSignInClient myClient;





    public loginActionImp(){

        myAuthUser  = FirebaseAuth.getInstance();

    }









    @Override
    public GoogleSignInClient googleLogIn(Context myContext){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(myContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


      myClient = GoogleSignIn.getClient(myContext, gso);

        return myClient;



    }
    @Override
    public void startLogin(){

    }



    @Override
    public void carrySignIn(GoogleSignInAccount account, final Context myContext, final ProgressBar myBar){


        myBar.setVisibility(View.VISIBLE);
        Utils.toastMessage("Sign In Successful", myContext );
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        myAuthUser  = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        myAuthUser.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");



                             FirebaseUser user = myAuthUser.getCurrentUser();
                             FirebaseFirestore myRef = FirebaseFirestore.getInstance();


                             User currUser = new User();





                             Map< String, Object > userData  = new HashMap<>();



                            // set values we know on user model
                            currUser.setEma(user.getEmail());
                            currUser.setName(user.getDisplayName());




                            currUser.setUserID(user.getUid());
                            currUser.setStrikeNum(0);

                                            userData.put("userID", currUser.getUserID());
                                            userData.put("name", currUser.getName());
                                            userData.put("strikeNum", currUser.getStrikeNum());
                                            userData.put("ema", currUser.getEma());

                                            // store in users->uid->user info

                                            myRef.collection("users").document(currUser.getName()).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Intent intent = new Intent(myContext, MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    myContext.startActivity(intent);


                                                }
                                            });




                                }


                    }
                });

    }

    @Override
    public void authUser() {
         currUser = myAuthUser.getCurrentUser();
    }






    //Methods That handle Sign in for firebase

    public void registerAccount(String email, String password, Context cont){

        if(checkConnectionAndInput(email,password,cont)==false)
        {
            return;
        }
        // Creating a new user
        if(email.contains("@gmail.com")) {

            Utils.toastMessage("Use Google Sign In", cont);
        }else {

            Utils.toastMessage("Account Created", cont);
            myAuthUser.createUserWithEmailAndPassword(email, password);

        }
    }

    public void fireSignMe(String email, String password, final Context cont, final ProgressBar myBar, final View mView){


        if(checkConnectionAndInput(email,password, cont)==false)
        {
            myBar.setVisibility(mView.INVISIBLE);
            return;
        }
        myAuthUser.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, Log a message to the LogCat. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful())
                        {
                            myBar.setVisibility(mView.INVISIBLE);
                            // There was an error
                            Utils.toastMessage("Incorrect Email/Password",cont);
                        }
                        else {
                            Utils.toastMessage("Sign In Successful", cont );
                            myBar.setVisibility(mView.VISIBLE);
                            FirebaseUser user = myAuthUser.getCurrentUser();
                            FirebaseFirestore myRef = FirebaseFirestore.getInstance();

                            final User currUser = new User();





                             Map< String, Object > userData  = new HashMap<>();



                            // set values we know on user model
                            currUser.setEma(user.getEmail());
                            currUser.setName(user.getDisplayName());







                                        currUser.setUserID(user.getUid());

                                        currUser.setStrikeNum(0);

                                        userData.put( "userID", currUser.getUserID());
                                        userData.put("name", currUser.getName());
                                        userData.put("strikeNum", currUser.getStrikeNum());
                                        userData.put("ema", currUser.getEma());

                                        // store in users->uid->user info

                                        myRef.collection("users").document(currUser.getName()).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(cont,MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                cont.startActivity(intent);


                                            }
                                        });









                            changeToMainScreen(cont);
                        }
                    }
                });


    }



    @Override
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


    //navigates to the main screen
    public void changeToMainScreen(final Context cont)
    {


        Intent next_activity = new Intent(cont, MainActivity.class);
        next_activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cont.startActivity(next_activity);
        finish();
    }

    public void resetPass(final Context cont){


    }


}
