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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Messages extends Fragment implements messagesAdapter.recyclerDetails, messagesAdapter.recyclerRating{
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
        myData.collection("notiAgreement").get()
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


        tittleAlert= (TextView)  alertTextOne.findViewById(R.id.alertTitle);

        moneyAlert= (TextView) alertTextOne.findViewById(R.id.costofDetails);
        buttonOK = (TextView) alertTextOne.findViewById(R.id.ButtonOk);




        myNoti = adapter.setNoti(pos);


        myData.collection("notiAgreement").document(myNoti.getTimeStamp()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                        DocumentSnapshot document = task.getResult();

                        if(task.isSuccessful()){

                            //get money plus details and set them to the according string

                            myDetails= (String) document.get("message");
                            myCost =  (String) document.get("money");

                            //set the string values to mour textviews
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



        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Rate Applicant");


        builder.setMessage("Thank you for rating us , it will help us to provide you the best service .");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Float value = myRatingAction.getRating();
                Utils.toastMessage(value.toString(),getContext());
            }
        });
        builder.setCancelable(false);
        builder.setView(ratingAlert);
        builder.show();

    }


    @Override
    public void onClick(View view, int position) {

        beginDetails(position);
    }

    @Override
    public void onMyClick(View v, int pos) {
        beginRatingAction(pos);

    }
}

