package com.example.jonny.n0t3s.tabView.applicants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jonny.n0t3s.Notification;
import com.example.jonny.n0t3s.R;

import java.util.List;


public class applicantAdapter extends ArrayAdapter<Notification> {


    public List<Notification> applicantList;
    private Context adapterCont;

    TextView postName,
            postTitle,
            acceptApp,
            declineApp;

    recyclerAccept myAccept;
    recyclerDeny myDeny;
    Notification myNotification;

    public interface recyclerAccept {
        void onClick(View view, int position);
    }


    public interface recyclerDeny{
        void onMyClick(View v, int pos);
    }
    public applicantAdapter(@NonNull Context context, List<Notification> myApplicants) {
        super(context,0, myApplicants);

        this.adapterCont = context;
        this.applicantList = myApplicants;

    }
    public void setRecycleraccept(recyclerAccept listener) {
        this.myAccept = listener;
    }


    public void setRecyclerDeny(recyclerDeny listener){
        this.myDeny = listener;
    }


    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.applicantsview_item, parent, false
            );
        }

        final Notification currentApplicant = getItem(position);


        //instiate our views
        postName = (TextView)listItemView.findViewById(R.id.nameID);
        postTitle = (TextView)listItemView.findViewById(R.id.notesTitle);
        acceptApp = (TextView)listItemView.findViewById(R.id.acceptPost);
        declineApp = (TextView)listItemView.findViewById(R.id.declinePost);

        postName.setText(currentApplicant.getSenderNoti());
        postTitle.setText(currentApplicant.getMessageNoti());



        //deleteicon listener deletes the data
        acceptApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myAccept != null){
                    myAccept.onClick(v, position);
                }


            }
        });


        //set like item code
        declineApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDeny != null){
                    myDeny.onMyClick(v, position);
                }



            }


        });

        return listItemView;


    }


    public List<Notification> getApplicantList(){
        return applicantList;
    }

    public Notification setNoti(int pos){
        myNotification = applicantList.get(pos);
        return myNotification;
    }









}