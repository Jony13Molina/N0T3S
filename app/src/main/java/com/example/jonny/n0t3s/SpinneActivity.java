package com.example.jonny.n0t3s;

import android.widget.AdapterView;
import android.view.View;

import com.example.jonny.n0t3s.addInfo.UI.addInfo;

/**
 * Created by jonny on 10/12/2017.
 */

public class SpinneActivity extends addInfo implements AdapterView.OnItemSelectedListener{
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id){

        parent.getItemAtPosition(pos);
    }
    public void onNothingSelected(AdapterView<?> parent){
        System.out.println("Nothing Was Selected");
    }


}