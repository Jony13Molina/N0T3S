package com.example.jonny.n0t3s;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonny.n0t3s.Main.MainActivity;
import com.example.jonny.n0t3s.Main.RecyclerTwoAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.sendbird.android.BaseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.jonny.n0t3s.addInfo.UI.addInfo.pToastMessage;

public class messagingActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView chatRecycler;
    private ChatListAdapter chatAdapter;
    private List<Notification> chattingList = new ArrayList<>();

    private List<Notification> tempChattingList = new ArrayList<>();

    Map <String, Object> chatMap = new HashMap<>();
    FirebaseFirestore myCollection = FirebaseFirestore.getInstance();
    Button sendMsgButton;
    Notification notificationMsg = new Notification();
    EditText msgPrompt;
    ListenerRegistration firestoreUpdate;
    ArrayList<String> chatData = new ArrayList<>();
    ArrayList<String> chatMessages = new ArrayList<>();
    ArrayList<String> chatReceiver = new ArrayList<>();

    List<Notification> chatList = new ArrayList<>();

    FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
    private static String timeS, titleChat;

    Notification messageSent;
    Notification receiverMsgSent;
    private static final String TAG = "MessagingActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);




        sendMsgButton = (Button)findViewById(R.id.sendmsg);

        msgPrompt = (EditText) findViewById(R.id.entermsg);




        sendMsgButton.setOnClickListener(messagingActivity.this);

        chatRecycler = (RecyclerView) findViewById(R.id.msglistrecycler);


            //Log.d("data", notificationMsg.toString());




        Intent timeStamp = getIntent();
        timeS = Objects.requireNonNull(timeStamp.getExtras()).getString("timeStamp");

        Log.d("timestampmeeeee", timeS);

        getData();


        updateListener();

    }

    @Override
    public void onResume() {


        super.onResume();

       getData();
       updateListener();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (firestoreUpdate!= null) {
            firestoreUpdate.remove();
            firestoreUpdate = null;
        }
    }
    @Override
    public void onClick(View v) {





        myCollection.collection("notiAgreement"+fireUser.getEmail()).document(timeS).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                        String ownerEmail   = (String) document.getString("ownerEmail");;
                        String rEmail = (String) document.getString("senderEmail");
                        String  titleC = (String) document.getString("from");
                        String ownerName = (String) document.getString("ownerName");
                        String receiverName = (String) document.getString("receiverName");



                        titleChat = titleC;


                    if(fireUser.getDisplayName().equals(receiverName)) {
                        sendMsg( rEmail, titleC, receiverName);

                        Log.d("this call", "this got called");
                    }else{
                        sendMsg(ownerEmail, titleC, ownerName);
                        Log.d("this other thing called", "we here dudeeeee");

                    }

                    getData();
                    //;
                    updateListener();

                }
            }
        });



    }

    public void sendMsg(final String passedEmail,
                        final String titleNoti, final String namePassed){


        //Checking if document alreayd exists and if no we create new document
        //else we update the document

       final String msgDetails = msgPrompt.getText().toString();
        /*myCollection.collection("chat"+titleNoti).document(passedEmail)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();


                String chatMsg = " ";
                if (document.exists()) {


                    notificationMsg = new Notification();
                    //chatData.add(msgDetails);
                    Log.d("this is the data", chatData.toString());
                    //chatData.add(msgDetails);

                    chatMessages = (ArrayList<String>)document.get(namePassed);

                    for (int i = 0; i < chatMessages.size(); i++) {


                        chatMsg = chatMessages.get(i);
                        chatData.add(chatMsg);

                    }
                    notificationMsg.setSentMsg(msgDetails);

                    chatMessages.add(notificationMsg.getSentMsg());

                    //userChats.put("chat"+Utils.timeStampMe(),notificationMsg.getSentMsg());
                    //we update
                    chatMap.put(fireUser.getDisplayName(), chatMessages);
                    myCollection.collection("chat"+titleNoti).
                            document(passedEmail).set(chatMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    msgPrompt.getText().clear();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "ERROR" + e.toString(),
                                            Toast.LENGTH_SHORT).show();

                                    msgPrompt.getText().clear();
                                    Log.d("TAG", e.toString());

                                }
                            });



                }else {

                    notificationMsg = new Notification();
                    notificationMsg.setSentMsg(msgDetails);



                    chatData.add(notificationMsg.getSentMsg());

                    //userChats.put("chat"+Utils.timeStampMe(), notificationMsg.getSentMsg());

                    //msgSent = notificationMsg.getSentMsg();
                    //userMessages.put("chat", chatData);
                   // chatMap.put("sentMsg", msgSent);
                    //userMessages.put("email", notificationMsg.getEma());



                    chatMap.put(namePassed, chatData);


                    myCollection.collection("chat"+titleNoti).
                            document(passedEmail).set(chatMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    msgPrompt.getText().clear();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "ERROR" + e.toString(),
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", e.toString());

                                }
                            });
                }
            }

        });*/






        String timeStamp = Utils.timeStampMe();
        notificationMsg = new Notification();
        notificationMsg.setSentMsg(msgDetails);

       notificationMsg.setTimeStamp(timeStamp);
       notificationMsg.setEma(passedEmail);
       notificationMsg.setName(namePassed);



        //chatData.add(notificationMsg.getSentMsg());

        //userChats.put("chat"+Utils.timeStampMe(), notificationMsg.getSentMsg());

        //msgSent = notificationMsg.getSentMsg();
        //userMessages.put("chat", chatData);
        // chatMap.put("sentMsg", msgSent);
        //userMessages.put("email", notificationMsg.getEma());



        chatMap.put("sentMsg", notificationMsg.getSentMsg());
        chatMap.put("ownerName", notificationMsg.getName());
        chatMap.put("timeStamp", notificationMsg.getTimeStamp());
        chatMap.put("ownerEmail", notificationMsg.getEma());
        //chatMap.put(namePassed, chatData);


        myCollection.collection("chat"+titleNoti).
                document(Utils.timeStampMe()+namePassed).set(chatMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        msgPrompt.getText().clear();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "ERROR" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());

                    }
                });




    }




    //listener to update messaging in real time

    public void updateListener(){

        myCollection.collection("notiAgreement"+fireUser.getEmail()).document(timeS).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    String  titleC = (String) document.getString("from");




                    firestoreUpdate = myCollection.collection("chat"+titleC)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {



                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {





                                    populateChatListener(value);



                                }
                            });




                }
            }
        });




    }

    public void getData(){

        myCollection.collection("notiAgreement"+fireUser.getEmail()).document(timeS).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String  sEmail   = (String) document.getString("ownerEmail");;
                    String rEmail = (String) document.getString("senderEmail");
                    String titleC = (String) document.getString("from");
                    String ownerName = (String) document.getString("ownerName");
                    String receiverName = (String) document.getString("receiverName");



                    getDataChat(titleC);




                }
            }
        });


    }
    //fetch data on the back end
    public void getDataChat(String titleC /*final String ownerE, final String receiverE, final String ownName, final String recName*/){



        myCollection.collection("chat"+titleC)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {




                        if(task.isSuccessful()) {


                            populateChatList(task);


                        }
                    }
                });
        //get data for the receiver

        /*myCollection.collection("chat"+titleC).document(receiverE).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if(document.exists()) {

                        Log.d("we are here", "we out hereiiiiiiiiiiii");

                        populateChatList(document, receiverE, ownerE, recName, ownName );

                    }


                }
            }




        });*/


        //fetch data for the owner
        /*myCollection.collection("chat"+titleC).document(ownerE).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if(document.exists()) {

                        Log.d("we are here", "we out hereaaaaaaa");

                        populateChatList(document, receiverE, ownerE, recName, ownName);

                    }


                }






            }




        });*/








    }


    //pass a doc ref and fetch the data with the listener and the
    public void populateChatList(Task<QuerySnapshot> queryRef){// String emailR, String emailO, String nameR, String nameO){



        Log.d("now we are here", "we out here nowww");







            Log.d("now we are here", "we out here dude");



        Log.d("this is data", "here dudeeeeee");
        List<Notification> msgList = new ArrayList<>();

        for (DocumentSnapshot myDoc : queryRef.getResult()) {
            Notification chatMsgs = myDoc.toObject(Notification.class);
            chatMsgs.setSentMsg((String) myDoc.get("sentMsg"));
            chatMsgs.setEma((String)myDoc.get("ownerEmail"));
            chatMsgs.setSenderEmail((String)myDoc.get("ownerName"));


            Log.d("we  data", myDoc.getId());



            msgList.add(chatMsgs);


        }

        chatAdapter = new ChatListAdapter(messagingActivity.this, msgList);

        //chatRecycler.setLayoutManager(new LinearLayoutManager(messagingActivity.this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());


        chatRecycler.setLayoutManager(mLayoutManager);
        chatRecycler.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
            //chatList.clear();



            /*if((ArrayList<String>) docRef.get(nameO) != null) {



                Log.d("now we are here", "we out here nowww");
                chatData = (ArrayList<String>) docRef.get(nameO);

                Log.d("arrayList", chatData.toString());


                    String chats = "";
                    String showData = "";

                    Log.d("print chat", chatData.toString());
                    //chatList = new ArrayList<>();
                    for (int i = 0; i < chatData.size(); i++) {

                        messageSent = new Notification();

                        chats = chatData.get(i);


                        messageSent.setSentMsg(chats);
                        messageSent.setSenderEmail(nameO);
                        messageSent.setEma(emailO);


                        chatList.add(messageSent);

                    }









            }*/



            //chattingList.clear();

            /*if((ArrayList<String>) docRef.get(nameR) != null){

                chatReceiver = (ArrayList<String>) docRef.get(nameR);

                String chats = "";
                String showData = "";

                for (int i = 0; i < chatReceiver.size(); i++) {

                    receiverMsgSent = new Notification();
                    chats = chatReceiver.get(i);



                    receiverMsgSent.setSentMsg(chats);


                    receiverMsgSent.setSenderEmail(nameR);
                    receiverMsgSent.setEma(emailR);



                    chatList.add(receiverMsgSent);
                }







            }*/










    }


    //populate chatlistener

    public void populateChatListener(QuerySnapshot queryRef){


        List<Notification> msgList = new ArrayList<>();

        for (DocumentSnapshot myDoc : queryRef) {
            Notification chatMsgs = myDoc.toObject(Notification.class);
            chatMsgs.setSentMsg((String) myDoc.get("sentMsg"));
            chatMsgs.setEma((String)myDoc.get("ownerEmail"));
            chatMsgs.setSenderEmail((String)myDoc.get("ownerName"));


            Log.d("we  data", myDoc.getId());



            msgList.add(chatMsgs);


        }

        chatAdapter = new ChatListAdapter(messagingActivity.this, msgList);

        //chatRecycler.setLayoutManager(new LinearLayoutManager(messagingActivity.this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());


        chatRecycler.setLayoutManager(mLayoutManager);
        chatRecycler.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();

    }


}