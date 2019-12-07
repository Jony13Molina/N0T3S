package com.example.jonny.n0t3s.Login;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;

import com.example.jonny.n0t3s.Main.MainActivity;
import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
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

public class loginActionImp extends FragmentActivity implements loginAction, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth myAuthUser;
    FirebaseUser currUser;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private static GoogleApiClient myClient;


    public loginActionImp(){
        myAuthUser  = FirebaseAuth.getInstance();

    }







    @Override
    public GoogleApiClient googleLogIn(Context myContext){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(myContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        myClient = new GoogleApiClient.Builder(myContext)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        return myClient;



    }
    @Override
    public void startLogin(){

    }



    @Override
    public void signInReq(int rqCode, int rsCode, Intent data,Context cont){

        if (rqCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                carrySignIn(account, cont);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }


    }

    @Override
    public void carrySignIn(GoogleSignInAccount account,  final Context myContext){


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
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

                            User currUser = new User();








                            // set values we know on user model
                            currUser.setEma(user.getEmail());
                            currUser.setName(user.getDisplayName());
                            currUser.setUserID(user.getUid());

                            // store in users->uid->user info

                            myRef.child("users").child(user.getUid()).setValue(currUser);


                            Intent intent = new Intent(myContext,MainActivity.class);
                            myContext.startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });

    }

    @Override
    public void authUser() {
         currUser = myAuthUser.getCurrentUser();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
