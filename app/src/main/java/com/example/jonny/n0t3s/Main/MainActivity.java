package com.example.jonny.n0t3s.Main;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import com.example.jonny.n0t3s.Login.UI.LoginActivity;
import com.example.jonny.n0t3s.jobsCompleted.jobsCompleted;
import com.example.jonny.n0t3s.rating.ratingActivity;
import com.example.jonny.n0t3s.reportUser;
import com.example.jonny.n0t3s.tabView.tabView;

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
import androidx.appcompat.app.AlertDialog;
import com.example.jonny.n0t3s.BuildConfig;
import com.example.jonny.n0t3s.Notification;
import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.User;
import com.example.jonny.n0t3s.Utils;
import com.example.jonny.n0t3s.addInfo.UI.addInfo;
import com.example.jonny.n0t3s.viewInfo.viewInfo;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;



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

    int likeCount = 0;
    Intent next_activity = null;
    String userPath, pathId;
    String countVal;

    boolean likeState = true;
    GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "MainActivity";



    List <String> myLikes = new ArrayList<>();



    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView userName, ratingField, emptyPosts;
    private View headerView;



    Notification noti = new Notification();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //getWindow().
        recyclerView = findViewById(R.id.recyclerList);
        myData = FirebaseFirestore.getInstance();
        mainUser = FirebaseAuth.getInstance();
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        myPresenter = new MainPresentImp(this);


        //variables for the drawer
        NavigationView myView = findViewById(R.id.nav_view);


        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        headerView = myView.getHeaderView(0);
        userName = headerView.findViewById(R.id.nav_header_textView);
        ratingField = headerView.findViewById(R.id.nav_header_numRate);
        emptyPosts = findViewById(R.id.emptyPublicposts);





//        mGoogleSignInClient.connect();
        setNavigation(toolbar,myView,recyclerView);



        //get the googleapi client to sign in







        //likeCount = 0;

        setUserName();
        getData();

        dataListner();




        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();






        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


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


    }


    @Override
    protected void onStop() {
        super.onStop();

        if (firestoreUpdate!= null) {
            firestoreUpdate.remove();
            firestoreUpdate = null;
        }
    }

    //get the data from database to populate our list
    private void getData(){
        myData.collection("Notes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {



                                List<User> userList = new ArrayList<>();

                                for (DocumentSnapshot myDoc : task.getResult()) {
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



                                if(userList.isEmpty()){
                                    emptyPosts.setVisibility(View.VISIBLE);
                                }else {
                                    emptyPosts.setVisibility(View.INVISIBLE);
                                }








                        } else {
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



            updateMyLike(pos);




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

                        if(userList.isEmpty()){
                            emptyPosts.setVisibility(View.VISIBLE);
                        }else {
                            emptyPosts.setVisibility(View.INVISIBLE);
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
    public void updateMyLike(int  pos) {



        user.setUserLike(likeState);





        user = adapter.setUser(pos);
        fireUser = FirebaseAuth.getInstance().getCurrentUser();

       // user.setUserID(fireUser.getUid());
        userPath = user.gettimeStampMe()+user.getTitle();
        //Log.d("timestamo!!!!!!!!!!!!", user.gettimeStampMe());
        pathId = "Notes";










        Log.d("this is the path", fireUser.getEmail()+user.getEma()+user.gettimeStampMe());
       final DocumentReference myReference = myData.collection("ApplicantsOf"+user.getEma()).document(fireUser.getEmail()+user.gettimeStampMe());



       myReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
           @Override
           public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

               if(!documentSnapshot.exists()){



                   ///

                   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                   alertDialogBuilder.setTitle("Are you interested in this post?");
                   alertDialogBuilder.setPositiveButton("Yes",
                           new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {







                                   //listen to the value of likeCounter in our adatabase and update accordingly
                                   myData.collection(pathId).document(userPath).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                       @Override
                                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                           if (task.isSuccessful()) {
                                               DocumentSnapshot document = task.getResult();
                                               if (document.exists()) {

                                                   String cVal =  (String) document.getString("likeCounter");


                                                   int cValue = Integer.parseInt(cVal) ;

                                                   if(cValue == 0){
                                                       likeCount = 0;
                                                       likeCount++;


                                                       countVal = Integer.toString(likeCount);
                                                       user.setLikeCounter(countVal);


                                                       Log.d("this is 1st like", countVal);
                                                       myPresenter.setLikeDatabase(pathId, userPath, user.getLikeCounter(), user.getUserLike());
                                                       likeState = true;


                                                       user.setUserLike(likeState);
                                                   }else {

                                                       likeCount = 1;
                                                       likeCount = likeCount+cValue;
                                                       countVal = Integer.toString(likeCount);
                                                       user.setLikeCounter(countVal);

                                                       Log.d("this is any like", countVal);

                                                       myPresenter.setLikeDatabase(pathId, userPath, user.getLikeCounter(), user.getUserLike());
                                                       likeState = true;


                                                       user.setUserLike(likeState);

                                                   }

                                               }
                                           }

                                       }
                                   });







                                   //myLikes.add(fireUser.getEmail());

                                   myPresenter.sendNotification(noti, user);


                               }
                           });
                   alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface arg0, int arg1) {
                       }
                   });
                   AlertDialog alertDialog = alertDialogBuilder.create();
                   alertDialog.show();


                   //

               }else{




                   Utils.toastMessage("Post Was Liked, You Can Only Like A Post Once!", MainActivity.this);



               }
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

                        //transition over to applicnt/application tab view
                    case R.id.applicantsID:
                        next_activity = new Intent(MainActivity.this, tabView.class);
                        startActivity(next_activity);
                        break;

                    case R.id.completeList:
                        next_activity = new Intent(MainActivity.this, jobsCompleted.class);

                        startActivity(next_activity);
                        break;
                    case R.id.topUsers:
                        next_activity = new Intent(MainActivity.this, ratingActivity.class);

                        startActivity(next_activity);
                        break;

                    case R.id.reportUsers:
                        next_activity = new Intent(MainActivity.this, reportUser.class);
                        startActivity(next_activity);
                        break;
                    case R.id.exitApp:
                        //logout
                        logOut();
                        //finish();
                        System.exit(0);
                        break;



                }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;


            }
        });



    }




    public void logOut(){

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        next_activity = new Intent(MainActivity.this, LoginActivity.class);
                        next_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(next_activity);
                        finish();
                    }
                });



        

    }




    //navigation drawer methods
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    //handling oriantation changes of the drawer
    @Override
    public void onConfigurationChanged(Configuration newCon){
        super.onConfigurationChanged(newCon);
        toggle.onConfigurationChanged(newCon);
    }

    //handling item selection on the menu drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //on back button pressed do nothing

    //on back button pressed do nothing
    @Override
    public void onBackPressed() {
        //do nothing

    }

    //set username
    //method to get the user name from firebase and then to set it on to the textview
    public void setUserName() {
        mainUser = FirebaseAuth.getInstance();
        fireUser = mainUser.getCurrentUser();


        userName.setText(fireUser.getEmail());

        myData.collection("userRatings").document(fireUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    Double cVal =  (Double) document.get("ratingValue");

                    String myCount = String.valueOf(cVal);

                    ratingField.setText(myCount);

                }
            }
        });

    }






}
