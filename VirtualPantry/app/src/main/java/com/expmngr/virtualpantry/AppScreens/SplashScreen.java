package com.expmngr.virtualpantry.AppScreens;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import com.expmngr.virtualpantry.BuildConfig;
import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.Database.FoodDatabase;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.DataImporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.room.Room;

public class SplashScreen extends AppCompatActivity {
    public static FoodDatabase database;
    private static final String CHANNEL_ID="expiry_notification";
    private static final String CHANNEL_NAME="Expiry notification";
    private static final String CHANNEL_DESC="Food expiry notifications";

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
                notifications();
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


    private void notifications(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        List<Food> foods = database.foodDAO().getFood();
        try{
            for(Food f:foods){
                String expDate = f.getExpiryDate();
                int hours = getHoursTillExpiry(expDate);
                if(hours < 0){
                    Date date = new SimpleDateFormat("dd/MM/yyyy HH").parse(expDate);

                    scheduleNotification(getNotification("expired"), date);
                }

            }

        } catch(Exception e){};

    }
    public void scheduleNotification(Notification notification, Date date) {

        Intent notificationIntent = new Intent(this, com.expmngr.virtualpantry.Utils.NotificationPublisher.class);
        notificationIntent.putExtra(com.expmngr.virtualpantry.Utils.NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(com.expmngr.virtualpantry.Utils.NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, new Date().getTime(), pendingIntent);
    }




    private Notification getNotification(String content){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Food Expiry reminder")
                .setContentText("Check your pantry for expiry")
                .setSmallIcon(R.drawable.ic_food)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent appActivityIntent = new Intent(this, ViewPantry.class);
        PendingIntent contentAppActivityIntent =
                PendingIntent.getActivity(
                        this,  // calling from Activity
                        0,
                        appActivityIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentAppActivityIntent);
        return builder.build();

    }


    private static int getHoursTillExpiry(String expiryDate) throws ParseException {
        Date expDate = new SimpleDateFormat("dd/MM/yyyy HH").parse(expiryDate);
        Date now = new Date();
        int difference = (int) (expDate.getTime() - now.getTime());
        difference = difference / 1000 / 60 / 60;

        return difference;
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
