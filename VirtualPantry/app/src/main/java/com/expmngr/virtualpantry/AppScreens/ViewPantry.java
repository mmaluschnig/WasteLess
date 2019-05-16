package com.expmngr.virtualpantry.AppScreens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.Database.Entities.FoodGroup;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewPantry extends AppCompatActivity {
    
    private static final int ACTIVITY_NUM = 2;
    List<Food> food;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pantry);

        setupBottomNavigationView();

        RecyclerView rvFood = findViewById(R.id.rvFood);


        food = MainMenuPlaceholder.database.foodDAO().getFood();
        final FoodAdapter adapter = new FoodAdapter(food);
        rvFood.setAdapter(adapter);
        rvFood.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(getApplicationContext(), ShoppingList.class));
                food.get(position).setName("Clicked");
                adapter.notifyItemRemoved(position);
                adapter.notifyItemChanged(position);

            }
        });

        setUpButtons();
    }




        //pantryItemsTextView = (TextView) findViewById(R.id.pantryItemsTextView);

        //List<Food> food = MainMenuPlaceholder.database.foodDAO().getFood();


//        String info = "";
//
//        for(Food f : food){
//            int id = f.getId();
//            String name = f.getName();
//            float quantity = f.getQuantity();
//            String expDate = f.getExpiryDate();
//            String addedDate = f.getDate_added();
//            Boolean hasExpired=true;
//
//            int timeTillExp;
//            int howOld;
//            String loc = f.getLocation();
//            try {
//                timeTillExp = getTimeBetween(expDate);
//                howOld = getAge(f.getDate_added());
//                if(timeTillExp < 0){
//                    f.setIsExpired(true);
//                    hasExpired = f.getIsExpired();
//                }
//                else {
//                    f.setIsExpired(false);
//                    hasExpired = f.getIsExpired();
//                }
//
//                MainMenuPlaceholder.database.foodDAO().updateFood(f);
//
//
//
//
//
//
//            } catch (ParseException e) {
//                timeTillExp = 0;
//                howOld = 0;
//                e.printStackTrace();
//            }
//            info = info + name + " Expires on: " + expDate + " " + loc + "\n\n";
//            //info = info + "\nID : " + id + "\nName : " + name + "\nLocation :" + loc+ "\nQuantity : " + quantity + "\nExpires on: " + expDate + " (" + timeTillExp + " hours)\nAdded: " + addedDate + " (" + howOld + " hours old)\n " + "has Expired: " + hasExpired ;
//        }


        //pantryItemsTextView.setText(info);



    private void setUpButtons(){

        Button filterByPantry = (Button) findViewById(R.id.pantryButton);
        //filterByPantry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                List<Food> food = MainMenuPlaceholder.database.foodDAO().getPantryFood();
//
//                addFoodToTextView(food);
//
//            }
        //});

        Button filterByFridge = (Button) findViewById(R.id.fridgeButton);
        //filterByFridge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                List<Food> food = MainMenuPlaceholder.database.foodDAO().getFridgeFood();
//
//                addFoodToTextView(food);
//            }
        //});

        Button filterByFreezer = (Button) findViewById(R.id.freezerButton);
        //filterByFreezer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                List<Food> food = MainMenuPlaceholder.database.foodDAO().getFreezerFood();
//
//                addFoodToTextView(food);
//            }
        //});

    }

    private void addFoodToTextView(List<Food> food){
        String info = "";

        for(Food f : food){
            int id = f.getId();
            String name = f.getName();
            float quantity = f.getQuantity();
            String expDate = f.getExpiryDate();
            String addedDate = f.getDate_added();
            Boolean hasExpired=true;

            int timeTillExp;
            int howOld;
            String loc = f.getLocation();
            try {
                timeTillExp = getTimeBetween(expDate);
                howOld = getAge(f.getDate_added());
                if(timeTillExp < 0){
                    f.setIsExpired(true);
                    hasExpired = f.getIsExpired();
                }
                else {
                    f.setIsExpired(false);
                    hasExpired = f.getIsExpired();
                }
            } catch (ParseException e) {
                timeTillExp = 0;
                howOld = 0;
                e.printStackTrace();
            }
            MainMenuPlaceholder.database.foodDAO().updateFood(f);

            info = info + name + " \nExpires on: " + expDate + "\n " + loc + "\nisExpired:" + f.getIsExpired() + "\n\n";
        }
        //pantryItemsTextView.setText(info);
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

    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigationViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(ViewPantry.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}