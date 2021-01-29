package com.example.jonny.n0t3s.tabView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.jonny.n0t3s.Main.MainActivity;
import com.example.jonny.n0t3s.Month;
import com.example.jonny.n0t3s.Notification;
import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.Utils;
import com.example.jonny.n0t3s.messagingActivity;
import com.example.jonny.n0t3s.rating.ratingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Iterables;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Messages extends Fragment implements messagesAdapter.recyclerDetails,
        messagesAdapter.recyclerRating, messagesAdapter.recyclerCompleted, messagesAdapter.recyclerMessage{
    messagesAdapter adapter;


    private String title, myDetails, myCost, ownerEmail, senderEmail;
    private int page;

    ListView agreementList;



    double maxRatingValue = 5;
    int totalRatingusers = 0;
    int counterRating = 0;
    Month myMonth = new Month();
    String applicantCom;
    String ownerCom;
    RatingBar myRatingAction;
    Notification myNoti = new Notification();
    View alertTextOne, ratingAlert;


    int jobCount;

    private TextView myNpotesDetails, buttonOK, ownerField,senderField, moneyAlert;



    FirebaseFirestore myData= FirebaseFirestore.getInstance();

    FirebaseAuth mainUser;

    FirebaseUser myUser = FirebaseAuth.getInstance().getCurrentUser();

    ///sstoring userRating
    final Map<String, Object> userRating  = new HashMap<>();
    final Map<String, Object> completedJobs  = new HashMap<>();

    final Map<String, Object> chatMessage = new HashMap<>();


    ArrayList <Double> listOfRatings = new ArrayList<Double>();

    TextView messagesEmpty, myHeader;
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

        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();
       // updateRating(myUser.getEmail(), 5.0);



        getData();
    }



    @Override
    public void onStop() {
        super.onStop();
    }






    @Override
    public void onResume(){
        super.onResume();
        Bundle savedInstanceState = new Bundle();

    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View messagesApplications = inflater.inflate(R.layout.messages_xmlfragment, container, false);

        agreementList = (ListView) messagesApplications.findViewById(R.id.messages_viewitem);



        View myView = getLayoutInflater().inflate(R.layout.headerviewapplicants,null);
        myHeader = myView.findViewById(R.id.headerApplicants);
        //TextView textView = new TextView(ratingActivity.this);

        myHeader.setText("Accepted Jobs");

        agreementList.addHeaderView(myHeader);
        messagesEmpty = messagesApplications.findViewById(R.id.msgEmpty);

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



                            if(userList.isEmpty()){
                                messagesEmpty.setVisibility(View.VISIBLE);
                            }else {
                                messagesEmpty.setVisibility(View.INVISIBLE);
                            }
                            adapter = new messagesAdapter(getContext(), userList);
                            adapter.setRecyclerDetails(Messages.this);
                            adapter.setRecyclerRating(Messages.this);
                            adapter.setRecyclerCompleted(Messages.this);
                            adapter.setRecyclerMsg(Messages.this);
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

        ownerField = (TextView) alertTextOne.findViewById(R.id.ownerEmail);
        senderField = (TextView) alertTextOne.findViewById(R.id.senderEmail);



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
                            ownerEmail = (String) document.get("ownerEmail");
                            senderEmail = (String) document.get("senderEmail");

                            //set the string values to our textviews
                            ownerField.setText("Post Owner:"+" "+ownerEmail);
                            senderField.setText("Accepted Applicant:"+" "+senderEmail);
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

    public void beginRatingAction(int position, final View view){

        ratingAlert = (View) getLayoutInflater().inflate(R.layout.ratinglayout, null);
        myRatingAction = (RatingBar) ratingAlert.findViewById(R.id.ratingBar);

        myNoti = adapter.setNoti(position);

        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();


       final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        //Log.d("ownerEMeail", myNoti.getOwnerEmail());

        myData.collection("notiAgreement"+myUser.getEmail()).document(myNoti.getTimeStamp()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                final DocumentSnapshot document = task.getResult();

                boolean myState = (boolean) document.get("buttonState");
                myNoti.setButtonState(myState);

                Log.d("buttonState", String.valueOf(myNoti.getButtonState()));


                if(!myNoti.getButtonState()) {
                    if (myUser.getEmail().equals(myNoti.getOwnerEmail())) {
                        builder.setTitle("Rate Applicant");
                        builder.setMessage("Rating Users Allows Us to Make a User Friendly Community.");


                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Float value = myRatingAction.getRating();


                                myNoti.setButtonState(true);


                                myData.collection("notiAgreement" + myNoti.getOwnerEmail())
                                        .document(myNoti.getTimeStamp()).update("buttonState", myNoti.getButtonState());
                                totalRatingusers++;

                                Utils.toastMessage(value.toString(), getContext());
                                Log.d("this is owner", myNoti.getOwnerEmail());
                                sendRating(value, myNoti.getSenderEmail(), totalRatingusers);
                                // updateRating(myNoti.getSenderEmail(), totalRatingusers*5);

                            }
                        });
                        builder.setCancelable(false);
                        builder.setView(ratingAlert);
                        builder.show();


                        //adapter.ratingApp.setVisibility(view.GONE);

                    } else if (myUser.getEmail().equals(myNoti.getSenderEmail())) {

                        builder.setTitle("Rate Employer");
                        builder.setMessage("Rating users creates a better community");


                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Float value = myRatingAction.getRating();

                                myData.collection("notiAgreement"+myNoti.getOwnerEmail())
                                        .document(myNoti.getTimeStamp()).update("buttonState", myNoti.getButtonState());

                                totalRatingusers++;
                                Utils.toastMessage(value.toString(), getContext());
                                Log.d("this is sender", myNoti.getSenderEmail());
                                sendRating(value, myNoti.getOwnerEmail(), totalRatingusers);

                            }
                        });
                        builder.setCancelable(false);
                        builder.setView(ratingAlert);
                        builder.show();

                        //adapter.ratingApp.setVisibility(view.GONE);

                    }
                } else {
                    Utils.toastMessage("You have already rated this job", getContext());


                }

            }

        });




    }

    //send rating to database

    public void sendRating(final double ratingValue, final String userEmail, final int totalRatingusers) throws NumberFormatException{


         final Double rValue = ratingValue;


        //listen to the value of likeCounter in our adatabase and update accordingly
       myData.collection("userRatings").document(userEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    String email = userEmail;
                    final DocumentSnapshot document = task.getResult();


                        if (document.exists()) {


                            ArrayList <Double> myValues = new ArrayList<>();
                            listOfRatings = (ArrayList<Double>) document.get("ratings");
                            //listOfRatings.addAll(myValues);
                            myNoti.setOwnerEmail(email);
                            myNoti.setRatingValue(rValue);
                            //counterRating++;



                            listOfRatings.add(myNoti.getRatingValue());
                            userRating.put("ratings", listOfRatings);
                            //userRating.put("ratingValue"+counterRating, myNoti.getRatingValue());
                            userRating.put("ownerEmail", myNoti.getOwnerEmail());


                            DocumentReference ratingRef = myData.collection("userRatings").document(userEmail);
                            ratingRef.update("ratings", FieldValue.arrayUnion(myNoti.getRatingValue()));


                            updateRating(userEmail, listOfRatings.size() * 5, listOfRatings);

                            Log.d("this is counter", String.valueOf(listOfRatings.size()));




                        } else {



                            myNoti.setOwnerEmail(email);
                            myNoti.setRatingValue(rValue);
                            counterRating++;
                            listOfRatings.add(myNoti.getRatingValue());
                            userRating.put("ratings", listOfRatings);
                            //userRating.put("ratingValue", myNoti.getRatingValue());
                            userRating.put("ownerEmail", myNoti.getOwnerEmail());
                            myData.collection("userRatings").document(email).set(userRating)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Utils.toastMessage("Rating submitted successfully ", getContext());

                                            Log.e("for shizzle rizzle", "we out");
//Log.d("THIS IS THE TOKEN!!!!", user.getUserToken());


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Utils.toastMessage("Error!!!" + e.toString(), getContext());

                                        }
                                    });


                            updateRating(userEmail, counterRating * 5, listOfRatings);
                        }


                    }
                 }



            
        });

    }



    public void updateRating(final String userEmail, final double maxRatingPossible, final ArrayList <Double> myList){




        myData.collection("userRatings").document(userEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {



                DocumentSnapshot document = task.getResult();


                //if(document.exists()){


                    double myRatingValue;
                    double sumRating = 0;
                    double myNum = 0;
                   // myList = (ArrayList) document.get("ratings");






                Log.d("this arraylist", String.valueOf(myList));

                    for(int i = 0; i < myList.size(); i++){

                        sumRating = myList.get(i);



                        myNum+=sumRating;

                        Log.d("this is the val inside1", String.valueOf(sumRating));

                        Log.d("this is the val inside", String.valueOf(myNum));


                    }

                   
                    myRatingValue = (myNum*maxRatingValue)/maxRatingPossible;
                Log.d("this is the num inside", String.valueOf(myNum));

                Log.d("this is the val", String.valueOf(myRatingValue));
                    userRating.put("ratingValue",myRatingValue);

                    myData.collection("userRatings").document(userEmail).set(userRating)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                   // Utils.toastMessage("Rating submitted successfully ", getContext());

                                    //Log.e("for shizzle rizzle","we out");
//Log.d("THIS IS THE TOKEN!!!!", user.getUserToken());


                                   // updateRating(userEmail, maxRatingPossible);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Utils.toastMessage("Error!!!" + e.toString(), getContext());

                                }
                            });



                }




            //}
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
        Utils.toastMessage("Both parties have agreed job was completed", getContext());
    }

    public void beginMsgView(int position){

        myNoti = adapter.setNoti(position);


        Log.d("timeStamp", myNoti.getTimeStamp());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Do you want to send a message");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        Intent timeStamp = new Intent(getActivity(), messagingActivity.class);
                        timeStamp.putExtra("timeStamp", myNoti.getTimeStamp());
                        startActivity(timeStamp);




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
        beginRatingAction(pos, v);



    }

    @Override
    public void onMyClickButton(View v, int pos) {

        beginCompletedAction(pos);
    }

    @Override
    public void onMsgClick(View v, int pos) {
        beginMsgView(pos);
    }
}

