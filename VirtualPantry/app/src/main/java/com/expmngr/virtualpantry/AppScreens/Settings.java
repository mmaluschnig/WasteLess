package com.expmngr.virtualpantry.AppScreens;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.expmngr.virtualpantry.AppScreens.AddItems.FeedbackScreen;
import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class Settings extends AppCompatActivity {
    //notification channel
    //notification builder
    //notification manager

    private static final String CHANNEL_ID="expiry_notification";
    private static final String CHANNEL_NAME="Expiry notification";
    private static final String CHANNEL_DESC="Food expiry notifications";

    private static final int ACTIVITY_NUM = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupBottomNavigationView();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        List<Food> food = MainMenuPlaceholder.database.foodDAO().getFood();

        int timeTillExp;
        long timeTillExpms;
        int howOld;


        for(Food f:food){
            int id = f.getId();
            String name=f.getName();
            String expDate = f.getExpiryDate();
            String addedDate = f.getDate_added();


            try {
                timeTillExp = getTimeBetween(expDate);
                timeTillExpms = timeTillExp*60*60*1000;

            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                howOld = getAge(f.getDate_added());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        Button feedbackBtn = (Button) findViewById(R.id.settingsOpenFeedbackPage);
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FeedbackScreen.class);
                startActivity(intent);
            }
        });

        Button fiveseconds = (Button) findViewById(R.id.action_5);
        fiveseconds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleNotification(getNotification("5 seconds delay"), 5);
            }
        });

        Button tenseconds = (Button) findViewById(R.id.action_10);
        tenseconds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleNotification(getNotification("10 second delay"), 10);
            }
        });


        Button thirtyseconds = (Button) findViewById(R.id.action_30);
        thirtyseconds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleNotification(getNotification("30 second delay"), 30);
            }
        });


    }



    private int getTimeBetween(String stringDate1, String stringDate2) throws ParseException {
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate1);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate2);
        int difference = (int)((date2.getTime() - date1.getTime()) / 1000 / 60 / 60);

        return difference;
    }
    private int getTimeBetween(String stringDate2) throws ParseException {
        Date date2 = new SimpleDateFormat("dd/MM/yyyy HH").parse(stringDate2);
        int difference = (int)(date2.getTime() - new Date().getTime()) / 1000 / 60 / 60 ;

        return difference;
    }

    private int getAge(String stringDate) throws ParseException {
        Date date2 = new SimpleDateFormat("dd/MM/yyyy HH").parse(stringDate);
        int difference = (int)((new Date().getTime() - date2.getTime()) / 1000 / 60 / 60 );

        return difference;
    }




    public void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, com.expmngr.virtualpantry.Utils.NotificationPublisher.class);
        notificationIntent.putExtra(com.expmngr.virtualpantry.Utils.NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(com.expmngr.virtualpantry.Utils.NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = ((System.currentTimeMillis())/1000 + delay);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }


    private Notification getNotification(String content){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Food Expiry reminder")
        .setContentText("Check your pantry for expiry")
        .setSmallIcon(R.drawable.ic_food)
        .setPriority(NotificationCompat.PRIORITY_HIGH);
        return builder.build();

    }




    private void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigationViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(Settings.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
