package com.example.jonny.n0t3s.tabView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jonny.n0t3s.tabView.applicants.Applicants;


public class ViewPagerAdapter extends FragmentStateAdapter {
        private static final int CARD_ITEM_SIZE = 3;
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        @NonNull @Override public Fragment createFragment(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return Applicants.newInstance(0, "Applicants");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return Applications.newInstance(1, "Applications");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return Messages.newInstance(2, "Accepted");
                default:
                    return null;


            }
        }
        @Override public int getItemCount() {
            return CARD_ITEM_SIZE;
        }
    }

