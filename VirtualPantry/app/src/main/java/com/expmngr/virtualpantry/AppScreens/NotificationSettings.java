package com.expmngr.virtualpantry.AppScreens;

import android.os.Bundle;

import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static java.util.Calendar.getInstance;

public class NotificationSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        List<Food> notifylist;
        List<Food> foodlist = MainMenuPlaceholder.database.foodDAO().getFood();

        for(Food f:foodlist){
            String expdate = f.getExpiryDate();
            Date c = getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String todaysDate = df.format(c);


               }
            }



        }


