package com.example.jonny.n0t3s.tabView.applicants;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;


public class Applicants extends Fragment implements applicantAdapter.recyclerAccept, applicantAdapter.recyclerDeny {

    applicantAdapter adapter;

    FirebaseFirestore myData= FirebaseFirestore.getInstance();

    List<Notification>myAppicants;
    FirebaseAuth mainUser;
    ListView myApplicants;
    Notification myNoti = new Notification();
    FirebaseUser myUser;


    String myDetails;
    String myTitle;
    String myCost;
    String time;
    String postOwnerEmail, ownerName;
    String senderEmail, receiverName;
    TextView emptyText, myHeader;

    private String title;
    private int page;

    ///storing ntofication agreement
    final Map<String, Object> notificationAgreement  = new HashMap<>();

    public static Applicants newInstance(int page, String title) {
        Applicants fragmentFirst = new Applicants();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);


        return fragmentFirst;
    }
        public Applicants() {
            // Required empty public constructor
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





            View applicantView = inflater.inflate(R.layout.view_pager_text, container, false);


           // getData();
            myApplicants = (ListView) applicantView.findViewById(R.id.applicantsView);
            getData();

            emptyText = applicantView.findViewById(R.id.emptyApplicantposts);

            View myView = getLayoutInflater().inflate(R.layout.headerviewapplicants,null);
            myHeader = myView.findViewById(R.id.headerApplicants);
            //TextView textView = new TextView(ratingActivity.this);

            myHeader.setText("My Applicants");

            myApplicants.addHeaderView(myHeader);
            return applicantView;



        }





    //get the data from database to populate our list
    public void getData(){
        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();

        //String mydata;
        myData.collection("ApplicantsOf"+myUser.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            List<Notification>  userList = new ArrayList<>();

                            for(DocumentSnapshot myDoc: task.getResult()){
                                Notification myApplicants = myDoc.toObject(Notification.class);
                                myApplicants.setMessageNoti((String)myDoc.get("senderEmail"));
                                myApplicants.setSenderNoti((String) myDoc.get("from"));



                                userList.add(myApplicants);

                            }


                            if(userList.isEmpty()){
                                emptyText.setVisibility(View.VISIBLE);
                            }else {
                                emptyText.setVisibility(View.INVISIBLE);
                            }



                            adapter = new applicantAdapter(getContext(), userList);
                            adapter.setRecycleraccept(Applicants.this);
                            adapter.setRecyclerDeny(Applicants.this);
                            myApplicants.setAdapter(adapter);




                        }else {
                            Log.d("TAG", "Error Getting Docs", task.getException());
                        }

                    }

                });










    }

    //method when the user deny applicnt
    public void denyApplicant(final int position){



        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();
        myNoti = adapter.setNoti(position);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Do you want to turn down the applicant");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d("my path is this brotha", myNoti.getSenderEmail()+myNoti.getTimeStamp());

                        myData.collection("ApplicantsOf"+myUser.getEmail()).document(
                                myNoti.getSenderEmail()+ myNoti.getTimeStamp()).delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        completeDeny( position);
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

    //this is called with denied
    public void completeDeny( int pos){
        //applicantList.remove(pos);
        adapter.getItem(pos);
        adapter.remove(adapter.getItem(pos));
        adapter.notifyDataSetChanged();
        Utils.toastMessage("Applicant was removed", getContext());
    }

    //this is called with applicant update


    public void completeUpdate( final  int pos){
        adapter.getItem(pos);
        adapter.remove(adapter.getItem(pos));
        adapter.notifyDataSetChanged();
        //Utils.toastMessage("Applicant was removed", getContext());
    }


    //this will take care of all the accepting logic
    //deletting post and notifying the post owner and applicant
    public void acceptApplicant(final int position){

        //send the notification to the post owner and also to the applicant

        myNoti = adapter.setNoti(position);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Do you want to accept this applicant");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        String myTime = myNoti.getTimeStamp();

                        completeAccept(myTime, myNoti, position);

                        //upDateApplicant(myNoti, position);
                        //eleteNotePublic(myTime);

                        //accept should remove the post and update messages list on messages fragment
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

    //this will delete the accepted applicant from our review to reduce useless items on our list
    public void upDateApplicant(Notification myNoti, final int pos){



        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();
        myData.collection("ApplicantsOf"+myUser.getEmail()).document(
                myNoti.getSenderEmail()+ myNoti.getTimeStamp()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        completeUpdate(pos);
                    }
                });
    }

    public void completeAccept(final String timeStamp, final Notification myNoti, final int pos ){


        mainUser = FirebaseAuth.getInstance();


        myUser = mainUser.getCurrentUser();


        final DocumentReference myReference = myData.collection("Notes").document(myNoti.getTimeStamp()+myNoti.getSenderNoti());


        myReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                if(documentSnapshot.exists()){
                    //let user know their operation completed succesfully
                    //Utils.toastMessage("Applicant was accepted", getContext());
                    myData.collection("ApplicantsOf"+myUser.getEmail()).document(myNoti.getSenderEmail()+myNoti.getTimeStamp()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){






                                        DocumentSnapshot document = task.getResult();
                                        if(document != null) {

                                            myDetails = (String) document.get("message");
                                            myTitle = (String) document.get("from");
                                            myCost = (String) document.get("money");
                                            time = (String) document.get("timeStamp");
                                            postOwnerEmail = (String) document.get("ownerEmail");
                                            ownerName = (String) document.get("ownerName");
                                            receiverName = (String) document.get("receiverName");


                                            //get user senderEmail

                                            senderEmail = myNoti.getSenderEmail();
                                            myNoti.setSenderNoti(myTitle);
                                            myNoti.setMessageNoti(myDetails);
                                            myNoti.setTimeStamp(time);
                                            myNoti.setOwnerEmail(postOwnerEmail);

                                            myNoti.setOwnerName(ownerName);
                                            myNoti.setReceiverName(receiverName);

                                            myNoti.setButtonState(false);
                                            //Log.d("this to agreement", myDetails);
                                            notificationAgreement.put("from", myNoti.getSenderNoti());
                                            notificationAgreement.put("message", myNoti.getMessageNoti());
                                            notificationAgreement.put("money", myCost);
                                            notificationAgreement.put("timeStamp", myNoti.getTimeStamp());
                                            notificationAgreement.put("senderEmail", senderEmail);
                                            notificationAgreement.put("ownerEmail", myNoti.getOwnerEmail());
                                            notificationAgreement.put("ownerName", myNoti.getOwnerName());
                                            notificationAgreement.put("receiverName", myNoti.getReceiverName());
                                            notificationAgreement.put("postOwnerCompleted", "No");
                                            notificationAgreement.put("applicantCompleted", "No");
                                            notificationAgreement.put("buttonState",myNoti.getButtonState());


                                            myData.collection("notiAgreement" + myNoti.getOwnerEmail()).document(myNoti.getTimeStamp()).set(notificationAgreement)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Utils.toastMessage("Applicant Accepted", getContext());
                                                            //Log.d("THIS IS THE TOKEN!!!!", user.getUserToken());


                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                            Utils.toastMessage("Error!!!" + e.toString(), getContext());

                                                        }
                                                    });


                                            myData.collection("notiAgreement" + myNoti.getSenderEmail()).document(myNoti.getTimeStamp()).set(notificationAgreement)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                           // Utils.toastMessage("Applicant Accepted", getContext());
                                                            //Log.d("THIS IS THE TOKEN!!!!", user.getUserToken());


                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                            Utils.toastMessage("Error!!!" + e.toString(), getContext());

                                                        }
                                                    });


                                            deleteNotePublic(myNoti.getTimeStamp(), myNoti.getSenderNoti());
                                            upDateApplicant(myNoti,pos);
                                        }
                                        if(document == null){
                                            Utils.toastMessage("Error", getContext());
                                        }




                                    }else {
                                        Log.d("TAG", "Error Getting Docs", task.getException());
                                    }

                                }

                            });

                }else{
                    Utils.toastMessage("Remainder, you can only accept one applicant per chore", getContext());



                }
            }
        });


    }

    public void deleteNotePublic(String timeStamp, String title){



        myData.collection("Notes").document(timeStamp+title).delete();

    }

    //onClick methods for the actions on the notes card
    //specifically accept and deny
    @Override
    public void onClick(View view, int position) {
        //setLike(position);

        myNoti = adapter.setNoti(position);

        acceptApplicant(position);


    }

    @Override
    public void onMyClick(View v, int pos) {
        //itemDelete(pos);

        denyApplicant(pos);

    }

}

