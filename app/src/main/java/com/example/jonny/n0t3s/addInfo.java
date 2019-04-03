package com.example.jonny.n0t3s;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.renderscript.Script;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.opencensus.tags.TagValue.MAX_LENGTH;

public class addInfo extends AppCompatActivity implements View.OnClickListener {

    Spinner listDrop;
    FloatingActionButton doneButton;
    FirebaseFirestore myCollection;
    private static final String Desc = "Description";
    private static final String Det = "Details";
    private static final String Year = "Year";
    FirebaseUser mainUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        //get a reference of our database
        myCollection = FirebaseFirestore.getInstance();
        //instantiate our drop down list
        listDrop = findViewById(R.id.dropDownls);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Year, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply adapter to Spinner
        listDrop.setAdapter(adapter);
        listDrop.setPrompt("SELECT A YEAR!!");

        //button view
        doneButton= (FloatingActionButton) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this);
    }
    /*public void onClick(View v)
    {
        if (v.getId() == R.id.doneButton)
        {
            try{
                //open file for writting
                OutputStreamWriter out = new OutputStreamWriter(openFileOutput("file.txt", MODE_APPEND));
                //initializing writing photo name
                EditText t_Edit = (EditText)findViewById(R.id.enterInfo);
                String Np_input = t_Edit.getText().toString();
                //initializing writing phtogragher name
                EditText p_Edit  = (EditText)findViewById(R.id.plusInfo);
                String Pn_Edit = p_Edit.getText().toString();
                //initializing writing year taken
                //listDrop = (Spinner)findViewById(R.id.dropDownls);

                String  Y_edit = listDrop.getSelectedItem().toString();
                //writing to file photo name, photographer, and year taken
                if( !Pn_Edit.equals("") && !Np_input.equals("")){
                    out.write(Np_input);
                    out.write(" ");
                    out.write(Pn_Edit);
                    out.write(" ");
                    out.write(Y_edit);
                    out.write('\n');

                    //close file
                    out.close();
                    Toast.makeText(this, "Special Notes Saved!", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(this, "Please don't live any fields blank",Toast.LENGTH_LONG).show();
                }


            }
            catch(IOException e) {
                //do something if an IOException occurs.
                Toast.makeText(this, "Sorry Text could't be added", Toast.LENGTH_LONG).show();

            }



        }
    }*/

   public void onClick(View v)
   {
        if (v.getId() == R.id.doneButton)
        {

                //initializing writing photo name
                EditText t_Edit = (EditText)findViewById(R.id.enterInfo);
                String Np_input = t_Edit.getText().toString();
                //initializing writing phtogragher name
                EditText p_Edit  = (EditText)findViewById(R.id.plusInfo);
                String Pn_Edit = p_Edit.getText().toString();
                //initializing writing year taken
                //listDrop = (Spinner)findViewById(R.id.dropDownls);

                //get drop down list item
                String  Y_edit = listDrop.getSelectedItem().toString();

                mainUser = FirebaseAuth.getInstance().getCurrentUser();
                //writing to database photo name, photographer, and year taken
                if( !Pn_Edit.equals("") && !Np_input.equals("")){
                    Map <String, Object> notes = new HashMap<>();
                    //notes.put("userID",mainUser.getUid());
                    notes.put(Desc, Np_input);
                    notes.put(Det, Pn_Edit);
                    notes.put(Year, Y_edit);


                    myCollection.collection("users").document(mainUser.getUid()+timeStampMe()).set(notes)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addInfo.this, "Special Notes Saved!", Toast.LENGTH_LONG).show();
                                    finish();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(addInfo.this, "ERROR" + e.toString(),
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", e.toString());

                                }
                            });

                }else{
                    Toast.makeText(this, "Please don't live any fields blank",Toast.LENGTH_LONG).show();
                }






        }
    }

    ///
    public static String timeStampMe() {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        return ts;
    }


}
