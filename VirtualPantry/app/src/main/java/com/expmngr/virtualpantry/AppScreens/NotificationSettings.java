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
            String todaysday = todaysDate.substring(0,2);
            String todaysmonth = todaysDate.substring(3,6);
            String todaysyear = todaysDate.substring(6,10);
            String expday = expdate.substring(0,2);
            String expmonth = expdate.substring(3,6);
            String expyear = expdate.substring(6,10);

            if((Integer.parseInt(expyear) == Integer.parseInt(todaysyear)) && Integer.parseInt(expmonth) == Integer.parseInt(todaysmonth)){

               if(Integer.parseInt(expday) > Integer.parseInt(todaysday)){
                   int diff = calculateDifference(Integer.parseInt(expday), Integer.parseInt(todaysday));

               }
            }



        }
    }

    public int calculateDifference(int something, int somethingelse){
        if(something>somethingelse){
            return something-somethingelse;
        }
        else if(something < somethingelse){
            return somethingelse-something;
        }
        else return 0;
    }
}
