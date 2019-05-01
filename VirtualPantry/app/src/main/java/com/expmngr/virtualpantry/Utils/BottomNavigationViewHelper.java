package com.expmngr.virtualpantry.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.expmngr.virtualpantry.AppScreens.AddNewItems;
import com.expmngr.virtualpantry.AppScreens.MainMenuPlaceholder;
import com.expmngr.virtualpantry.AppScreens.ViewPantry;
import com.expmngr.virtualpantry.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import androidx.annotation.NonNull;

public class BottomNavigationViewHelper {

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){

    }
    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()){
                   case R.id.nav_home:
                       Intent intent1 = new Intent(context, MainMenuPlaceholder.class);
                       context.startActivity(intent1);
                       break;

                   case R.id.nav_add:
                       Intent intent2 = new Intent(context, AddNewItems.class);
                       context.startActivity(intent2);

                       break;

                   case R.id.nav_view:
                       Intent intent3 = new Intent(context, ViewPantry.class);
                       context.startActivity(intent3);

                       break;

               }


                return false;
            }
        });
    }
}
