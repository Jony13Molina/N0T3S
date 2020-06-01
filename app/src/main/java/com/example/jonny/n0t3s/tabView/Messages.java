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

import com.example.jonny.n0t3s.Month;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Messages extends Fragment implements messagesAdapter.recyclerDetails, messagesAdapter.recyclerRating, messagesAdapter.recyclerCompleted{
    messagesAdapter adapter;


    private String title, myDetails, myCost;
    private int page;

    ListView agreementList;


    Month myMonth = new Month();
    String applicantCom;
    String ownerCom;
    RatingBar myRatingAction;
    Notification myNoti = new Notification();
    View alertTextOne, ratingAlert;

    int jobCount;

    private TextView myNpotesDetails, buttonOK, tittleAlert, moneyAlert;



    FirebaseFirestore myData= FirebaseFirestore.getInstance();

    FirebaseAuth mainUser;

    FirebaseUser myUser;

    ///sstoring userRating
    final Map<String, Object> userRating  = new HashMap<>();
    final Map<String, Object> completedJobs  = new HashMap<>();


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

    public void sendRating(final Float ratingValue, final String userEmail,final String timeStamp){





        //listen to the value of likeCounter in our adatabase and update accordingly
        myData.collection("userRatings").document(userEmail+timeStamp).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        String rVal =  (String) document.getString("ratingValue");


                        double rValue = Integer.parseInt(rVal);



                        if (rValue == 0) {


                            userRating.put("ratingValue", ratingValue);
                            userRating.put("userEmail", userEmail);
                            myData.collection("userRatings").document(userEmail).set(userRating)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Utils.toastMessage("Rating submitted successfully ", getContext());

//Log.d("THIS IS THE TOKEN!!!!", user.getUserToken());


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Utils.toastMessage("Error!!!" + e.toString(), getContext());

                                        }
                                    });





                        } else {

                            double numRating;
                            double finalNumRating;
                            numRating = rValue/5;
                            numRating = Math.ceil(numRating);


                            finalNumRating = rValue/numRating;

                            userRating.put("ratingValue", finalNumRating);
                            userRating.put("userEmail", userEmail);
                            myData.collection("userRatings").document(userEmail).set(userRating)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Utils.toastMessage("Rating submitted successfully ", getContext());

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


                    }else{

                        userRating.put("ratingValue", ratingValue);
                        userRating.put("userEmail", userEmail);
                        myData.collection("userRatings").document(userEmail).set(userRating)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Utils.toastMessage("Rating submitted successfully ", getContext());

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
                }

            }
        });

    }

    //this will start the action to indicate completed chore/job
    //will delete record once the two parties select completed action

    public void beginCompletedAction(final int position){


        myNoti = adapter.setNoti(position);

        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Has this job been completed");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        //update completed agreement from applicant side
                        myData.collection("notiAgreement"+myUser.getEmail()).document(myNoti.getTimeStamp()).update("applicantCompleted", "Yes");

                        //update applicant agreement from owner side
                        myData.collection("notiAgreement"+myUser.getEmail()).document(myNoti.getTimeStamp()).update("postOwnerCompleted", "Yes");


                        updateCompleted(myUser.getEmail());

                        myData.collection("notiAgreement"+myNoti.getSenderEmail()).document(myNoti.getTimeStamp()).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                                        DocumentSnapshot document = task.getResult();

                                        if(task.isSuccessful()){

                                            //get money plus details and set them to the according string



                                            applicantCom = (String) document.getString("applicantCompleted");


                                            myData.collection("notiAgreement"+myNoti.getOwnerEmail()).document(myNoti.getTimeStamp()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                                                            DocumentSnapshot document = task.getResult();

                                                            if(task.isSuccessful()){

                                                                //get money plus details and set them to the according string



                                                                ownerCom = (String) document.getString("postOwnerCompleted");

                                                                if(applicantCom.equals("Yes") && ownerCom.equals("Yes")){


                                                                    myData.collection("notiAgreement"+myNoti.getOwnerEmail()).document(
                                                                            myNoti.getTimeStamp()).delete()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {


                                                                                    myData.collection("notiAgreement"+myNoti.getSenderEmail()).document(
                                                                                            myNoti.getTimeStamp()).delete()
                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                                    continueCompleteAction(position);

                                                                                                }
                                                                                            });
                                                                                }
                                                                            });


                                                                    //continueCompleteAction(position);
                                                                }


                                                            }else {
                                                                Log.d("TAG", "Error Getting Docs", task.getException());
                                                            }

                                                        }
                                                    });

                                        }else {
                                            Log.d("TAG", "Error Getting Docs", task.getException());
                                        }

                                    }
                                });















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


    public void updateCompleted(String userEmail){


        String monthName;
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);

        monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        jobCount ++;
        uploadCompleted(monthName , jobCount);

    }

    public void uploadCompleted(final String monthName, final int count ){

        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();
        myData.collection("CompletedJobs"+myUser.getEmail()).document("comJobs"+monthName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String cVal =  (String) document.getString("jobCompCount");
                    int cValue = Integer.parseInt(cVal) ;



                        cValue+=count;
                        String myCount = String.valueOf(cValue);
                        completedJobs.put("monthName", monthName);
                        completedJobs.put("jobCompCount", myCount);
                        myData.collection("CompletedJobs"+myUser.getEmail())
                                .document("comJobs"+monthName).update("jobCompCount", myCount);

                }else {

                    myMonth.setMonthName(monthName);

                    String myCount = String.valueOf(count);
                    myMonth.setJobCompCount(myCount);
                    completedJobs.put("monthName", myMonth.getMonthName());
                    completedJobs.put("jobCompCount", myMonth.getJobCompCount());
                    myData.collection("CompletedJobs"+myUser.getEmail())
                            .document("comJobs"+monthName).set(completedJobs)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d("this is completed","complete");
                                }
                            });

                    }
            }
        });
    }


    public void continueCompleteAction(int pos){




        adapter.getItem(pos);
        adapter.remove(adapter.getItem(pos));
        adapter.notifyDataSetChanged();
        Utils.toastMessage("Both parties have completed this job", getContext());
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

