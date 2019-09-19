package com.example.jonny.n0t3s.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.jonny.n0t3s.Main.BottomNavigationViewHelper;
import com.example.jonny.n0t3s.Notification;
import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.User;
import com.example.jonny.n0t3s.Utils;
import com.example.jonny.n0t3s.addInfo.UI.addInfo;
import com.example.jonny.n0t3s.viewInfo.viewInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import com.pusher.pushnotifications.PushNotifications;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements MainView,
        RecyclerTwoAdapter.RecyclerDeleteButton, RecyclerTwoAdapter.RecyclerLikeButton{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore myData;
    FirebaseAuth mainUser;
    FirebaseUser fireUser;
    ListenerRegistration firestoreUpdate;
    MainPresentImp myPresenter;
    User user;
    RecyclerTwoAdapter adapter;
    int likeCount;
    String userPath, pathId;
    boolean likeState = true;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerList);
        myData = FirebaseFirestore.getInstance();
        //mainUser = FirebaseAuth.getInstance();
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        myPresenter = new MainPresentImp(this);

        getData();

        dataListner();


        setNavigation();









    }
    @Override
    protected void onStart(){
        super.onStart();
        getData();
    }

    //get all data that was newly added or shared ti the public list


    @Override
    protected void onResume() {
        super.onResume();
        getData();


    }


    //stop the listener when the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();

        firestoreUpdate.remove();
    }

    //get the data from database to populate our list
    private void getData(){
        myData.collection("Notes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            List<User> userList = new ArrayList<>();

                            for(DocumentSnapshot myDoc: task.getResult()){
                                User myUser = myDoc.toObject(User.class);
                                myUser.setUserID(myDoc.getId());
                                userList.add(myUser);

                            }

                            adapter = new RecyclerTwoAdapter(userList, MainActivity.this, myData);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            adapter.setRecyclerButton(MainActivity.this);
                            adapter.setRecyclerDeleteListener(MainActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                        }else {
                            Log.d("TAG", "Error Getting Docs", task.getException());
                        }

                    }
                });
    }



    //Begin Like action and update info
    @Override
    public void setLike(int pos) {
        user = adapter.setUser(pos);



        if (fireUser.getEmail().equals( user.getEma())) {

            Utils.toastMessage("Can't Like Your Own Posts", MainActivity.this);
        }else{

            updateMyLike(user, pos);



        }







    }


    //dekete item from the notes, first check if the user
    //requesting delete = user who made post
    @Override
    public void itemDelete(final int pos) {

        user = adapter.setUser(pos);
        if (fireUser.getEmail().equals( user.getEma())) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Are you sure you want to delete this note?");
            alertDialogBuilder.setPositiveButton("Delete",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myPresenter.startDelete(adapter.getUserNotes(), adapter.setUser(pos),adapter, pos);


                        }
                    });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        }else{
            Utils.toastMessage("Can't delete a post you didn't share", MainActivity.this);

        }
    }

    //listen to changes in the data
    @Override
    public void dataListner() {
        firestoreUpdate = myData.collection("Notes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }
                        List<User> userList = new ArrayList<>();

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            User myUserNotes = doc.toObject(User.class);
                            myUserNotes.setUserID(doc.getId());
                            userList.add(myUserNotes);
                        }

                        adapter = new RecyclerTwoAdapter(userList, MainActivity.this, myData);
                        adapter.setRecyclerDeleteListener(MainActivity.this);
                        adapter.setRecyclerButton(MainActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }
    //delete the note from the list
    //get the information and data from database first
    @Override
    public void deleteNote(List<User>userNotes,int pos, RecyclerTwoAdapter adapter, Context cont) {
        userNotes.remove(pos);
        adapter.notifyItemRemoved(pos);
        adapter.notifyItemRangeChanged(pos, userNotes.size());
        Utils.toastMessage("Note was deleted", cont);
    }

    @Override
    public void updateMyLike(final User user,final int  pos) {

        if (likeState) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Do you want to apply to this post?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            likeCount++;
                            final String countVal = Integer.toString(likeCount);
                            user.setLikeCounter(countVal);


                            fireUser = FirebaseAuth.getInstance().getCurrentUser();

                            user.setUserID(fireUser.getUid());
                            userPath = user.gettimeStampMe();
                            //Log.d("timestamo!!!!!!!!!!!!", user.gettimeStampMe());
                            pathId = "Notes";


                            likeState = false;
                            user.setUserLike(likeState);

                            myPresenter.setLikeDatabase(pathId, userPath, countVal, user.getUserLike());
                            Notification noti = new Notification();
                            myPresenter.sendNotification(noti, adapter.setUser(pos));

                        }
                    });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();






        } else {
            likeCount--;
            String countVal = Integer.toString(likeCount);
            user.setLikeCounter(countVal);

            fireUser = FirebaseAuth.getInstance().getCurrentUser();

            user.setUserID(fireUser.getUid());
            userPath = user.gettimeStampMe();
            pathId = "Notes";


            //user.setUserLike(true);
            likeState = true;
            user.setUserLike(likeState);
            myPresenter.setLikeDatabase(pathId, userPath, countVal, user.getUserLike());
        }
    }



    //methods that handle view and clicking actions for menu and note
    public void setNavigation()
    {
        BottomNavigationView  bottomNav = findViewById(R.id.navigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNav);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent next_activity = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_info:
                        //transition to input notes
                        next_activity = new Intent(MainActivity.this, addInfo.class);
                        startActivity(next_activity);
                        break;

                    case R.id.navigation_viewInfo:
                        //transition to Viewing info class
                        Intent intent = new Intent(MainActivity.this, viewInfo.class);
                        startActivity(intent);
                        break;

                    case R.id.navigation_exit:
                        //close app
                        finish();
                        System.exit(0);
                        break;


                }
                return true;
            }
        });
    }




    @Override
    public void onClick(View view, int position) {
        setLike(position);

    }

    @Override
    public void onMyClick(View v, int pos) {
        itemDelete(pos);

    }

    public void setTopic(String myTopic){

        FirebaseMessaging.getInstance().subscribeToTopic(myTopic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                          //  msg = getString(R.string.msg_subscribe_failed);
                        }
                       // Log.d(TAG, msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
