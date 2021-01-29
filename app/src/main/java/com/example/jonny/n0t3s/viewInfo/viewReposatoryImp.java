package com.example.jonny.n0t3s.viewInfo;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import android.util.Log;

import com.example.jonny.n0t3s.Month;
import com.example.jonny.n0t3s.User;
import com.example.jonny.n0t3s.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class viewReposatoryImp extends ContextWrapper implements viewReposatory {

    FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
    User user;
    public static Context myContext;
    String userPath;


    SharedPreferences mSharedPref;
    List<User> myUserList;
    List<User> userList;
    RecyclerAdapter myAdapter;
    FirebaseFirestore myCollection = FirebaseFirestore.getInstance();
    viewInfo myView;

    Month myMonth = new Month();

    int jobCount;

    private Map<String, String> monthData = new HashMap<>();
    final Map<String, Object> completedJobs  = new HashMap<>();
    public viewReposatoryImp(Context base) {
        super(base);
        myContext = base;
        myView = new viewInfo();

    }



    //delete note at position
    @Override
    public void deleteNoteAt(List<User> userNotes, User myUser, RecyclerAdapter adapter, int pos) {

        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        myUser.setUserID(fireUser.getUid());
        userPath = myUser.getUserID() + myUser.gettimeStampMe();
        itemDelete(userNotes, myUser.getUserID(), userPath, pos, adapter);


        //this is to get the current month date
        String monthName;
        Calendar calendar = Calendar.getInstance();


        monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+calendar.get(Calendar.YEAR);
        jobCount++;
        updateCompletedJob(fireUser.getEmail(),monthName,jobCount);
        Log.d("This is the path!", userPath);
    }

    //
    public void updateCompletedJob(final String email, final String month, final int count){

        myCollection.collection("CompletedJobs"+email).document("comJobs"+month).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String cVal =  (String) document.getString("jobCompCount");
                    int cValue = Integer.parseInt(cVal) ;



                    cValue+=count;
                    String myCount = String.valueOf(cValue);
                    completedJobs.put("monthName", month);
                    completedJobs.put("jobCompCount", myCount);
                    myCollection.collection("CompletedJobs"+email)
                            .document("comJobs"+month).update("jobCompCount", myCount);

                }else {

                    myMonth.setMonthName(month);    

                    String myCount = String.valueOf(count);
                    myMonth.setJobCompCount(myCount);
                    completedJobs.put("monthName", myMonth.getMonthName());
                    completedJobs.put("jobCompCount", myMonth.getJobCompCount());
                    myCollection.collection("CompletedJobs"+email)
                            .document("comJobs"+month).set(completedJobs)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d("this is completed","complete");
                                }
                            });

                }
            }
        });
    }
    //share notes
    @Override
    public void shareThisNote(User user,String moneyInput) {

        Map<String, Object> notes = new HashMap<>();
        //notes.put("userID",mainUser.getUid());

        moneyInput = "$"+moneyInput;


        Log.e("money amount 1000000", moneyInput);



        user.setUserLike(false);
        notes.put("title", user.getTitle());
        notes.put("name", user.getName());
        notes.put("details", user.getDetails());
        notes.put("year", user.getYear());
        notes.put("ema", user.getEma());
        notes.put("timeStampMe", user.gettimeStampMe());
        notes.put("likeCounter", "0");
        notes.put("money",  moneyInput);
        notes.put("userLike", user.getUserLike());




        myCollection.collection("Notes").document(user.gettimeStampMe()+user.getTitle()).set(notes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                           Utils.toastMessage("Special Note Shared", myContext);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Utils.toastMessage("Error!!!" + e.toString(), myContext);

                    }
                });


    }

    //delete notes
    @Override
    public void itemDelete(final List<User> userNotes, String id, String path,
                           final int pos, final RecyclerAdapter adapter) {

        myCollection = FirebaseFirestore.getInstance();

        myCollection.collection(id).document(path).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                      /*  userNotes.remove(pos);
                        adapter.notifyItemRemoved(pos);
                        adapter.notifyItemRangeChanged(pos, userNotes.size());
                        Utils.toastMessage("Note was deleted", myContext);*/

//                        myPresenter.deleteItems(userNotes, pos, adapter, myContext);
                        myView.itemDelete(userNotes, pos, adapter, myContext);
                    }
                });

    }


/*-----------------------------------------------------------------------------------------------/
/   This marks the end of methods related to adapter logic/action.                               /
/  This ,method is to get the data from fireStore and populate our view.                         /
/   This is a mehtod to get the user data                                                        /
/-----------------------------------------------------------------------------------------------*/

    public FirebaseFirestore getData() {


        myCollection = FirebaseFirestore.getInstance();


        return myCollection;



    }







}
