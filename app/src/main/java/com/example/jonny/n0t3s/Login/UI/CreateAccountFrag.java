package com.example.jonny.n0t3s.Login.UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class CreateAccountFrag extends Fragment {

    FirebaseFirestore myData= FirebaseFirestore.getInstance();

    FirebaseAuth mainUser;

    FirebaseUser myUser;


    TextView usernamePrompt;
    TextView passwordPrompt;
    FirebaseAuth myAuthUser = FirebaseAuth.getInstance();
    MaterialButton accountMake;

    Context cont = getContext();

    public static CreateAccountFrag newInstance() {
        CreateAccountFrag fragmentFirst = new CreateAccountFrag();

        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





       // getData();
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View createAccountView = inflater.inflate(R.layout.createaccountlayout, container, false);

        usernamePrompt= (TextView) createAccountView.findViewById(R.id.enterEmail);
        passwordPrompt = (TextView) createAccountView.findViewById(R.id.passwordtext);
        accountMake = (MaterialButton) createAccountView.findViewById(R.id.createUser);


        String email = usernamePrompt.getText().toString().trim();
        String password = passwordPrompt.getText().toString().trim();




        createAccount(email, password);
        return createAccountView;


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
            Utils.toastMessage("Check Internet Connection", getContext());
            return false;
        }
        else
        {
            return true;
        }

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


    public void createAccount(final String email,final String Passwd) {

        accountMake.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                registerAccount(email,Passwd,getContext());


                Intent next_activity = new Intent(getContext(), LoginActivity.class);
                next_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(next_activity);
                getActivity().finish();

            }
        });

    }

}
