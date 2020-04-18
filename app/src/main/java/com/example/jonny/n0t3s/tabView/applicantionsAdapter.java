package com.example.jonny.n0t3s.tabView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jonny.n0t3s.Notification;
import com.example.jonny.n0t3s.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

class applicantionsAdapter extends ArrayAdapter<Notification> {






    public List<Notification> applicantList;
    private Context adapterCont;

    TextView postName,
            postTitle,
            acceptApp,
            declineApp;


    recyclerDelete myDeny;
    Notification myNotification;



    public interface recyclerDelete{
        void onMyClick(View v, int pos);
    }
    public applicantionsAdapter(@NonNull Context context, List<Notification> myApplicants) {
        super(context,0, myApplicants);

        this.adapterCont = context;
        this.applicantList = myApplicants;

    }



    public void setRecyclerDelete(applicantionsAdapter.recyclerDelete listener){
        this.myDeny = listener;
    }


    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.applicationsview, parent, false
            );
        }

        final Notification currentApplicant = getItem(position);


        //instiate our views
        postName = (TextView)listItemView.findViewById(R.id.nameTitle);
        postTitle = (TextView)listItemView.findViewById(R.id.notesPost);
        declineApp = (TextView)listItemView.findViewById(R.id.deleteApp);

        postName.setText(currentApplicant.getSenderNoti());
        postTitle.setText(currentApplicant.getMessageNoti());





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
