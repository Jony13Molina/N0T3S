package com.example.jonny.n0t3s.tabView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.jonny.n0t3s.Notification;
import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Messages extends Fragment implements messagesAdapter.recyclerDetails, messagesAdapter.recyclerRating, messagesAdapter.recyclerCompleted{
    messagesAdapter adapter;


    private String title, myDetails, myCost;
    private int page;

    ListView agreementList;


    RatingBar myRatingAction;
    Notification myNoti = new Notification();
    View alertTextOne, ratingAlert;


    private TextView myNpotesDetails, buttonOK, tittleAlert, moneyAlert;



    FirebaseFirestore myData= FirebaseFirestore.getInstance();

    FirebaseAuth mainUser;

    FirebaseUser myUser;

    ///maps
    final Map<String, Object> userRating  = new HashMap<>();

    public Messages() {
        // Required empty public constructor
    }


    public static Messages newInstance(int page, String title) {
        Messages fragmentFirst = new Messages();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View messagesApplications = inflater.inflate(R.layout.messages_xmlfragment, container, false);

        agreementList = (ListView) messagesApplications.findViewById(R.id.messages_viewitem);



        getData();
        return messagesApplications;


    }




    public void getData(){

        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();

        //String mydata;
        myData.collection("notiAgreement"+myUser.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            List<Notification> userList = new ArrayList<>();

                            for(DocumentSnapshot myDoc: task.getResult()){
                                Notification myAgreements = myDoc.toObject(Notification.class);
                                //myAgreements.setMessageNoti((String)myDoc.get("ownerEmail"));
                                myAgreements.setMessageNoti((String) myDoc.get("from"));



                                userList.add(myAgreements);

                            }



                            adapter = new messagesAdapter(getContext(), userList);
                            adapter.setRecyclerDetails(Messages.this);
                            adapter.setRecyclerRating(Messages.this);
                            adapter.setRecyclerCompleted(Messages.this);
                            agreementList.setAdapter(adapter);




                        }else {
                            Log.d("TAG", "Error Getting Docs", task.getException());
                        }

                    }

                });



    }
    public void beginDetails(int pos){



        //get firebase auth and user instance

        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();


        //set the alert Dialog values

       AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
       alert.setTitle("You have Agreed to These Terms");
        alertTextOne = (View) getLayoutInflater().inflate( R.layout.detailsalert, null);

        myNpotesDetails = (TextView) alertTextOne.findViewById(R.id.detailsofPost);



        moneyAlert= (TextView) alertTextOne.findViewById(R.id.costofDetails);
        buttonOK = (TextView) alertTextOne.findViewById(R.id.ButtonOk);




        myNoti = adapter.setNoti(pos);


        myData.collection("notiAgreement"+myUser.getEmail()).document(myNoti.getTimeStamp()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                        DocumentSnapshot document = task.getResult();

                        if(task.isSuccessful()){

                            //get money plus details and set them to the according string

                            myDetails= (String) document.get("message");
                            myCost =  (String) document.get("money");

                            //set the string values to our textviews
                            myNpotesDetails.setText(myDetails);
                            moneyAlert.setText(myCost);



                        }else {
                            Log.d("TAG", "Error Getting Docs", task.getException());
                        }

                    }
                });



        alert.setView(alertTextOne);
        final AlertDialog alertDialog = alert.create();

        alertDialog.setCanceledOnTouchOutside(false);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });





        alertDialog.show();







    }

    public void beginRatingAction(int position){

        ratingAlert = (View) getLayoutInflater().inflate(R.layout.ratinglayout, null);
        myRatingAction = (RatingBar) ratingAlert.findViewById(R.id.ratingBar);

        myNoti = adapter.setNoti(position);

        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        //Log.d("ownerEMeail", myNoti.getOwnerEmail());

        if(myUser.getEmail().equals(myNoti.getSenderEmail())) {
            builder.setTitle("Rate Employer");
            builder.setMessage("Rating Users Allows Us to Make a User Friendly Community.");


            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Float value = myRatingAction.getRating();

                    Utils.toastMessage(value.toString(), getContext());
                    sendRating(value,myNoti.getOwnerEmail(), myNoti.getTimeStamp());
                }
            });
            builder.setCancelable(false);
            builder.setView(ratingAlert);
            builder.show();

        }else if(myUser.getEmail().equals(myNoti.getOwnerEmail())){

            builder.setTitle("Rate Applicant");
            builder.setMessage("Rating Users Allows Us to Make a User Friendly Community.");


            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Float value = myRatingAction.getRating();

                    Utils.toastMessage(value.toString(), getContext());
                    sendRating(value,myNoti.getSenderEmail(),myNoti.getTimeStamp());
                }
            });
            builder.setCancelable(false);
            builder.setView(ratingAlert);
            builder.show();

        }

    }

    //send rating to database

    public void sendRating(Float ratingValue, String userEmail,String timeStamp){


        userRating.put("ratingValue", ratingValue);
        userRating.put("userEmail", userEmail);
        myData.collection("userRatings").document(userEmail+timeStamp).set(userRating)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Utils.toastMessage("Rating submitted successfully ", getContext());

                        /************rating psudoCode
                         * send rating to database and divide by num of current rating
                         * ratings/totalNumRatings, get total number of ratings by creating a counter and updating
                         * wil require fetching data too
                         *
                         * get database collection rating ratingCounter+=totalNumberRatings
                         */
//Log.d("THIS IS THE TOKEN!!!!", user.getUserToken());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Utils.toastMessage("Error!!!" + e.toString(), getContext());

                    }
                });
    }

    //this will start the action to indicate completed chore/job

    public void beginCompletedAction(int position){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Has this job been completed?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {




                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    //clicking action buttons for rating and details
    @Override
    public void onClick(View view, int position) {

        beginDetails(position);
    }

    @Override
    public void onMyClick(View v, int pos) {
        beginRatingAction(pos);

    }

    @Override
    public void onMyClickButton(View v, int pos) {

        beginCompletedAction(pos);
    }
}

