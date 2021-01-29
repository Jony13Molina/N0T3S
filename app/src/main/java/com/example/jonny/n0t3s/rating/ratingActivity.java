package com.example.jonny.n0t3s.rating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.example.jonny.n0t3s.Notification;
import com.example.jonny.n0t3s.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ratingActivity extends AppCompatActivity{

    FirebaseFirestore myData= FirebaseFirestore.getInstance();

    EditText searchText;
    FirebaseAuth mainUser;

    ratingListAdapter adapter;

    FirebaseUser myUser;
    ListView ratingList;
    MenuItem searchMenu;
    SearchView sView;


    TextView myHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);


        Toolbar toolbar = findViewById(R.id.toolbar_rating);
        setSupportActionBar(toolbar);
        ratingList = (ListView) findViewById(R.id.ratingitemslist);

        View myView = getLayoutInflater().inflate(R.layout.headerlist_view,null);
        myHeader = myView.findViewById(R.id.headerTitle);
        //TextView textView = new TextView(ratingActivity.this);

        myHeader.setText("Ratings");

        ratingList.addHeaderView(myHeader);
        //listTitle = (TextView)findViewById(R.id.titleList);

        //listTitle.setText("Users:");
        /////searchText = (EditText) findViewById(R.id.search_username);
        getData();





    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchbox_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenu = menu.findItem(R.id.searchboxvview);
        sView = (SearchView) searchMenu.getActionView();

        sView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //sView.setSubmitButtonEnabled(true);
        //sView.setOnQueryTextListener(this);

        //ratingList.setTextFilterEnabled(true);

        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                 adapter.getFilter().filter(query);

                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {

                Log.e("Main"," data search"+newText);

                //ratingList.setFilterText(newText);

                adapter.getFilter().filter(newText);
                if (TextUtils.isEmpty(newText)) {
                    ratingList.clearTextFilter();

                } else {
                    ratingList.setFilterText(newText);
                }





                return true;
            }
        });


        return true;

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();


        if(id == R.id.searchboxvview){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void getData(){

        mainUser = FirebaseAuth.getInstance();
        myUser = mainUser.getCurrentUser();
        myData.collection("userRatings").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            List<Notification> userList = new ArrayList<>();

                            for(DocumentSnapshot myDoc: task.getResult()){
                                Notification myAgreements = myDoc.toObject(Notification.class);
                                myAgreements.setMessageNoti((String)myDoc.get("ownerEmail"));

                                double ratingValue = (double)(myDoc.get("ratingValue"));
                                //Double ratingVal = ratingValue.doubleValue();
                                myAgreements.setRatingValue( ratingValue);
                                //myAgreements.setOwnerEmail((String) myDoc.get("ownerEmail"));


//                                Log.d(myAgreements.getRatingValue().toString(),"this is rating Value");
                                userList.add(myAgreements);

                            }



                            adapter = new ratingListAdapter(ratingActivity.this, userList);

                            ratingList.setAdapter(adapter);






                        }else {
                            Log.d("TAG", "Error Getting Docs", task.getException());
                        }

                    }

                });
    }






}
