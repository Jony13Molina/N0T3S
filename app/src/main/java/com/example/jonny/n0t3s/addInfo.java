package com.example.jonny.n0t3s;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class addInfo extends AppCompatActivity implements View.OnClickListener {

    Spinner listDrop;
    FloatingActionButton doneButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);

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

    public void onClick(View v)
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
    }


}
