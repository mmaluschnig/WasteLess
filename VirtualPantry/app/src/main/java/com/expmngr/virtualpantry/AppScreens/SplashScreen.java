package com.expmngr.virtualpantry.AppScreens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;

import com.expmngr.virtualpantry.BuildConfig;
import com.expmngr.virtualpantry.Database.FoodDatabase;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.DataImporter;

public class SplashScreen extends AppCompatActivity {
    public static FoodDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
//        Thread myThread = new Thread(){
//            @Override
//            public void run() {
//                try {
//                    sleep(4000);
//                    Intent intent = new Intent(getApplicationContext(), MainMenuPlaceholder.class);
//                    startActivity(intent);
//                    finish();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
        //myThread.start();
        Thread importThread = new Thread(){
            @Override
            public void run() {
                long startTime = SystemClock.elapsedRealtime();
                database = Room.databaseBuilder(getApplicationContext(), FoodDatabase.class,"fooddb").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                checkFirstRun();
                long elapsedTime = SystemClock.elapsedRealtime() - startTime;
                if(elapsedTime <= 1000){
                    System.out.println("wait" + elapsedTime);
                    try {
                        sleep(1000 - elapsedTime);
                        Intent intent = new Intent(getApplicationContext(), MainMenuPlaceholder.class);
                        startActivity(intent);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println(elapsedTime);
                    Intent intent = new Intent(getApplicationContext(), MainMenuPlaceholder.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        importThread.start();
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
}
