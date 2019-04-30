package com.expmngr.virtualpantry.AppScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.MainActivity;
import com.expmngr.virtualpantry.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ViewPantry extends AppCompatActivity {
    TextView pantryItemsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pantry);

        pantryItemsTextView = (TextView) findViewById(R.id.pantryItemsTextView);

        List<Food> food = MainMenuPlaceholder.database.foodDAO().getFood();

        String info = "";

        for(Food f : food){
            int id = f.getId();
            String name = f.getName();
            float quantity = f.getQuantity();
            String expDate = f.getExpiryDate();
            String addedDate = f.getDate_added();

            int timeTillExp;
            int howOld;
            String loc = f.getLocation();
            try {
                timeTillExp = getTimeBetween(expDate);
                howOld = getAge(f.getDate_added());
            } catch (ParseException e) {
                timeTillExp = 0;
                howOld = 0;
                e.printStackTrace();
            }

            info = info + "\nID : " + id + "\nName : " + name + "\nQuantity : " + quantity + "\nExpires on: " + expDate.substring(0,10) + " (" + timeTillExp + " hours)\nAdded: " + addedDate.substring(0,10) + " (" + howOld + " hours old)\n";
        }
        pantryItemsTextView.setText(info);
    }

    private int getTimeBetween(String stringDate1, String stringDate2) throws ParseException {
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate1);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate2);
        int difference = (int)((date2.getTime() - date1.getTime()) / 1000 / 60 / 60);

        return difference;
    }
    private int getTimeBetween(String stringDate2) throws ParseException {
        Date date2 = new SimpleDateFormat("dd/MM/yyyy HH").parse(stringDate2);
        int difference = (int)Math.abs((date2.getTime() - new Date().getTime()) / 1000 / 60 / 60 );

        return difference;
    }

    private int getAge(String stringDate) throws ParseException {
        Date date2 = new SimpleDateFormat("dd/MM/yyyy HH").parse(stringDate);
        int difference = (int)((new Date().getTime() - date2.getTime()) / 1000 / 60 / 60 );

        return difference;
    }
}