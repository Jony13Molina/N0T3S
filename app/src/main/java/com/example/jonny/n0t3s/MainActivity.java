package com.example.jonny.n0t3s;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNavigation();
    }

    public void setNavigation()
    {
        BottomNavigationView  bottomNav = findViewById(R.id.navigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNav);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent next_activity = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_info:
                        //transition to input notes
                        next_activity = new Intent(MainActivity.this, addInfo.class);
                        startActivity(next_activity);
                        break;

                    case R.id.navigation_viewInfo:
                        //transition to Viewing info class
                        Intent intent = new Intent(MainActivity.this, viewInfo.class);
                        startActivity(intent);
                        break;

                    case R.id.navigation_exit:
                        //close app
                        finish();
                        System.exit(0);
                        break;


                }
                return true;
            }
        });
    }

}
