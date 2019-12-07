package com.example.jonny.n0t3s.Main;
import android.annotation.SuppressLint;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.util.Log;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import java.lang.reflect.Field;

// Helper class as a workaround to get bottom nav to stop shifting when icon is clicked
class BottomNavigationViewHelper
{

    @SuppressLint("RestrictedApi")
    static void removeShiftMode(BottomNavigationView view)
    {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        menuView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        menuView.buildMenuView();


    }
}


