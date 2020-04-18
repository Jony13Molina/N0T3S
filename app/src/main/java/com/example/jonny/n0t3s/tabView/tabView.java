package com.example.jonny.n0t3s.tabView;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.widget.ViewPager2;



import com.example.jonny.n0t3s.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class tabView extends AppCompatActivity {

    public TabLayout myTabs;
    public ViewPager2 myPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_view);

        myPager = findViewById(R.id.view_pager);
        myTabs = findViewById(R.id.tabs);


        myPager.setAdapter(createCardAdapter());
        new TabLayoutMediator(myTabs, myPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                        switch (position) {
                            case 0:
                                tab.setText("Applicants");


                                break;
                            case 1:
                                tab.setText("Application");
                                break;
                            case 2:
                                tab.setText("Messages");
                                break;
                        }

                    }

                }).attach();


    }

    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        return adapter;
    }
}













