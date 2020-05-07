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

public class messagesAdapter extends ArrayAdapter<Notification> {







    public List<Notification> applicantList;
    private Context adapterCont;

    TextView postName,
            postTitle,
            ratingApp,
            detailsApp,
            appDone;

    recyclerDetails myDetails;
    recyclerRating myRating;
    recyclerCompleted recyclerC;
    Notification myNotification;
    FirebaseFirestore myData;

    public interface recyclerDetails {
        void onClick(View view, int position);
    }


    public interface recyclerRating{
        void onMyClick(View v, int pos);
    }

    public interface recyclerCompleted{
        void onMyClickButton(View v, int pos);
    }



  public void setRecyclerDetails(recyclerDetails listener){
        this.myDetails = listener;

  }
  public void setRecyclerRating(recyclerRating listener){

        this.myRating = listener;
  }

  public void setRecyclerCompleted(recyclerCompleted listener){
        this.recyclerC = listener;
  }

    public messagesAdapter(@NonNull Context context, List<Notification> myApplicants) {
        super(context,0, myApplicants);

        this.adapterCont = context;
        this.applicantList = myApplicants;

    }


    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.messages_viewitem, parent, false
            );
        }

        final Notification currentApplicant = getItem(position);


        //instiate our views
        //postName = (TextView)listItemView.findViewById(R.id.notesPost);
        postTitle = (TextView)listItemView.findViewById(R.id.detailsPost);
        detailsApp = (TextView)listItemView.findViewById(R.id.notificationDetails);
        ratingApp = (TextView)listItemView.findViewById(R.id.notificationRating);
        appDone = (TextView)listItemView.findViewById(R.id.notificationCompleted);

        //postName.setText(currentApplicant.getSenderEmail());
        postTitle.setText(currentApplicant.getMessageNoti());



        //get the details from the owner
        detailsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDetails != null){
                    myDetails.onClick(v, position);
                }


            }
        });


        //set rating code
        ratingApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myRating != null){
                    myRating.onMyClick(v, position);
                }



            }


        });


        //set Completed button
        appDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(recyclerC != null){
                    recyclerC.onMyClickButton(v,position);
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






