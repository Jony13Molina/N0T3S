package com.example.jonny.n0t3s.tabView;

        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ListView;

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

public class Applications extends Fragment implements applicantionsAdapter.recyclerDelete  {

    private String title;
    private int page;
    applicantionsAdapter adapter;

    FirebaseFirestore myData= FirebaseFirestore.getInstance();

    List<Notification> myAppicants;
    FirebaseAuth mainUser;
    ListView myApplicants;
    Notification myNoti = new Notification();
    FirebaseUser myUser;
    public Applications() {
        // Required empty public constructor
    }

    public static Applications newInstance(int page, String title) {
        Applications fragmentFirst = new Applications();
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


        View applicationsView = inflater.inflate(R.layout.applications_xmlfragment, container, false);


        // getData();
        myApplicants = (ListView) applicationsView.findViewById(R.id.applicationsview);
        getData();


        return applicationsView;
    }



    public void getData(){

        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();

        //String mydata;
        myData.collection("personalApplications"+myUser.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            List<Notification>  userList = new ArrayList<>();

                            for(DocumentSnapshot myDoc: task.getResult()){
                                Notification myApplicants = myDoc.toObject(Notification.class);
                                myApplicants.setMessageNoti((String)myDoc.get("ownerEmail"));
                                myApplicants.setSenderNoti((String) myDoc.get("from"));



                                userList.add(myApplicants);

                            }



                            adapter = new applicantionsAdapter(getContext(), userList);
                            adapter.setRecyclerDelete(Applications.this);
                            myApplicants.setAdapter(adapter);




                        }else {
                            Log.d("TAG", "Error Getting Docs", task.getException());
                        }

                    }

                });



    }

    //method when the user deny applicnt
    public void deleteApplication(final int position){

        myNoti = adapter.setNoti(position);

        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();
        myNoti = adapter.setNoti(position);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Do you want to delete this item from your history");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d("my path is this brotha", myNoti.getSenderEmail()+myNoti.getTimeStamp());

                        myData.collection("personalApplications"+myUser.getEmail()).document(
                                 myNoti.getTimeStamp()).delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        completeDelete( position);
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

    public void completeDelete( int pos){
        //applicantList.remove(pos);
        adapter.getItem(pos);
        adapter.remove(adapter.getItem(pos));
        adapter.notifyDataSetChanged();
        Utils.toastMessage("Item was removed successfully ", getContext());
    }

    @Override
    public void onMyClick(View v, int pos) {
        deleteApplication(pos);

    }
}