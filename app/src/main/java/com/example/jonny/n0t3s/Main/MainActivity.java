package com.example.jonny.n0t3s.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import com.example.jonny.n0t3s.tabView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.jonny.n0t3s.BuildConfig;
import com.example.jonny.n0t3s.Notification;
import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.User;
import com.example.jonny.n0t3s.Utils;
import com.example.jonny.n0t3s.addInfo.UI.addInfo;
import com.example.jonny.n0t3s.viewInfo.viewInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements MainView,
        RecyclerTwoAdapter.RecyclerDeleteButton, RecyclerTwoAdapter.RecyclerLikeButton{

    RecyclerView recyclerView;
    NavigationView myview;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore myData;
    FirebaseAuth mainUser;
    FirebaseUser fireUser;
    ListenerRegistration firestoreUpdate;
    MainPresentImp myPresenter;
    User user;
    RecyclerTwoAdapter adapter;
     String countVal;
     int likeCount;
    String userPath, pathId;
    boolean likeState = true;
    private static final String TAG = "MainActivity";

    private static final String sharedPref = "token";

    List <String> myLikes = new ArrayList<>();

    final private String myServerKey = BuildConfig.ApiKey;
    final private String jsonContent = "application/json";
    final private String fcmSendAdress = BuildConfig.FcmAdress;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView userName;
    private View headerView;



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


        //variables for the drawer
        NavigationView myView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        headerView = myView.getHeaderView(0);
        userName = headerView.findViewById(R.id.nav_header_textView);


        setNavigation(toolbar,myView,recyclerView);



        setUserName();
        getData();

        dataListner();





        setToken();






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
        user.setUserLike(likeState);


        fireUser = FirebaseAuth.getInstance().getCurrentUser();

        user.setUserID(fireUser.getUid());
        userPath = user.gettimeStampMe();
        //Log.d("timestamo!!!!!!!!!!!!", user.gettimeStampMe());
        pathId = "Notes";





       final DocumentReference myReference = myData.collection("Notification").document(user.gettimeStampMe()).
                collection("applicants").document(fireUser.getEmail());


       myReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
           @Override
           public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
               if(documentSnapshot.exists()){
                  /* Log.d("Document exists", myReference.toString());
                   likeCount--;

                   String countVal = Integer.toString(likeCount);
                   user.setLikeCounter(countVal);

                   fireUser = FirebaseAuth.getInstance().getCurrentUser();

                   user.setUserID(fireUser.getUid());
                   userPath = user.gettimeStampMe();


                   //user.setUserLike(true);
                   likeState = true;
                   user.setUserLike(likeState);
                   myPresenter.setLikeDatabase(pathId, userPath, countVal, user.getUserLike());

                    */


                   ///


                   Utils.toastMessage("You Have Already Liked This Post", MainActivity.this);

               }else{


                   myData.collection("Notification").document(user.gettimeStampMe())
                           .collection("applicants").document(fireUser.getEmail()).delete();
                   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                   alertDialogBuilder.setTitle("Are you interested in this post?");
                   alertDialogBuilder.setPositiveButton("Yes",
                           new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {

                                   likeCount++;

                               countVal = Integer.toString(likeCount);
                                   user.setLikeCounter(countVal);


                                   likeState = false;
                                   user.setUserLike(likeState);

                                   myPresenter.setLikeDatabase(pathId, userPath, countVal, user.getUserLike());
                                   Notification noti = new Notification();
                                   myPresenter.sendNotification(noti, adapter.setUser(pos));


                                   myLikes.add(fireUser.getEmail());

                               }
                           });
                   alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface arg0, int arg1) {
                       }
                   });
                   AlertDialog alertDialog = alertDialogBuilder.create();
                   alertDialog.show();

                   Log.d("else it doesnt exists", "dont exit");
               }
           }
       });












    }



    //methods that handle view and clicking actions for menu and note


    public void setToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        SharedPreferences settings = getApplicationContext().
                                getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("token", token);
                        editor.commit();

                    }
                });

    }


    //onClick methods for the actions on the notes card
    //specifically like and delete
    @Override
    public void onClick(View view, int position) {
        setLike(position);

    }

    @Override
    public void onMyClick(View v, int pos) {
        itemDelete(pos);

    }



    //methods for drawer
    public void setNavigation(Toolbar toolbar, NavigationView myView,final RecyclerView rView) {


        //setting the hamburger icon open close
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);



            }
        };


        //makes hamburger icon clickable and add the
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        myView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                Intent next_activity = null;
                switch (menuItem.getItemId()) {
                    case R.id.newNote:
                        //transition to input notes
                        next_activity = new Intent(MainActivity.this, addInfo.class);
                        startActivity(next_activity);
                        break;

                    case R.id.privateList:
                        //transition to Viewing info class
                        next_activity = new Intent(MainActivity.this, viewInfo.class);
                        startActivity(next_activity);
                        break;

                    case R.id.applicantsID:
                        next_activity = new Intent(MainActivity.this, tabView.class);
                        startActivity(next_activity);
                        break;

                    case R.id.exitApp:
                        //close app
                        finish();
                        System.exit(0);
                        break;


                }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;


            }
        });



    }








    //navigation drawer
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newCon){
        super.onConfigurationChanged(newCon);
        toggle.onConfigurationChanged(newCon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //on back button pressed do nothing

    //setting up the click listner
    @Override
    public void onBackPressed() {
        //do nothing

    }

    //set username
    //method to get the user name from firebase and then to set it on to the textview
    public void setUserName(){
        mainUser = FirebaseAuth.getInstance();
        fireUser = mainUser.getCurrentUser();

         userName.setText(fireUser.getEmail());
    }









}
