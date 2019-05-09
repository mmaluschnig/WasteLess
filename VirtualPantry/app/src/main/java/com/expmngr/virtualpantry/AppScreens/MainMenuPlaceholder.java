package com.expmngr.virtualpantry.AppScreens;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.expmngr.virtualpantry.BuildConfig;
import com.expmngr.virtualpantry.Database.FoodDatabase;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.BottomNavigationViewHelper;
import com.expmngr.virtualpantry.Utils.DataImporter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class MainMenuPlaceholder extends AppCompatActivity {


    public static FoodDatabase database;
    private static final int ACTIVITY_NUM = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_placeholder);

        database = Room.databaseBuilder(getApplicationContext(), FoodDatabase.class,"fooddb").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        checkFirstRun();
        setUpButtons();
        setupBottomNavigationView();


    }






    private void setUpButtons(){
        Button addButton = (Button) findViewById(R.id.addItemButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddNewItems.class));
            }
        });

        Button pantryButton = (Button) findViewById(R.id.viewPantryButton);
        pantryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
                startActivity(new Intent(getApplicationContext(), ViewPantry.class));
            }
        });

        Button settingsButton = (Button) findViewById(R.id.viewSettingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });

        Button shoppingListButton = (Button) findViewById(R.id.viewShoppingListButton);
        shoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ShoppingList.class));
            }
        });

    }
    public void setAlarm()
    {

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent _ )
            {
                //TODO insert notification builder code here

                 

                System.out.println("ALARM>>>>>>>>>>>>>>>>>>>>>>>");
                context.unregisterReceiver( this ); // this =notification android studio example= BroadcastReceiver, not Activity
            }
        };

        this.registerReceiver( receiver, new IntentFilter("com.expmngr.virtualpantry.expiry") );

        PendingIntent pintent = PendingIntent.getBroadcast( this, 0, new Intent("com.expmngr.virtualpantry.expiry"), 0 );
        AlarmManager manager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));

        // set alarm to fire 5 sec (1000*5) from now (SystemClock.elapsedRealtime())
        manager.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000*5, pintent );
    }

    private void checkFirstRun() {

        final String PREFS_NAME = "FirstTimePrefs";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            System.out.println(">>>NORMAL RUN");

            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {
            System.out.println(">>>NEW INSTALL");
            //This is a new install (or the user cleared the shared preferences)
            DataImporter importer = new DataImporter(getApplicationContext());
            importer.addExpiryTimes();

        } else if (currentVersionCode > savedVersionCode) {
            System.out.println(">>>NEW VERSION");

            // This is an upgrade frozen vegetables
            DataImporter importer = new DataImporter(getApplicationContext());
            importer.addExpiryTimes();
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }



    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigationViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(MainMenuPlaceholder.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }
}
