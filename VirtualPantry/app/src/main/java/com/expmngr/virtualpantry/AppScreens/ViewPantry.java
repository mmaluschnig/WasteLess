package com.expmngr.virtualpantry.AppScreens;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ViewPantry extends AppCompatActivity {
    TextView pantryItemsTextView;
    Spinner filterSpinner;

    private String currentLocation;
    private String currentFilter;
    private String[] filters = {"date_added", "expiryDate", "category", "name", "quantity"};
    private static final int ACTIVITY_NUM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pantry);

        setupBottomNavigationView();
        setUpButtons();

        pantryItemsTextView = (TextView) findViewById(R.id.pantryItemsTextView);

        currentLocation = "All";
        currentFilter = filters[0];
        addFoodToTextView();


    }

    private void setUpButtons(){

        Button viewAllButton = (Button) findViewById(R.id.viewAllButton);
        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentLocation = "All";
                addFoodToTextView();

            }
        });

        Button filterByPantry = (Button) findViewById(R.id.pantryButton);
        filterByPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentLocation = "Pantry";
                addFoodToTextView();

            }
        });

        Button filterByFridge = (Button) findViewById(R.id.fridgeButton);
        filterByFridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentLocation = "Fridge";
                addFoodToTextView();
            }
        });

        Button filterByFreezer = (Button) findViewById(R.id.freezerButton);
        filterByFreezer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentLocation = "Freezer";
                addFoodToTextView();
            }
        });

        filterSpinner = (Spinner) findViewById(R.id.filterSpinner);
        ArrayAdapter<CharSequence> loc_adapter = ArrayAdapter.createFromResource(this, R.array.filter_options, android.R.layout.simple_spinner_item);
        loc_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(loc_adapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFilter = filters[position];
                addFoodToTextView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentFilter = filters[0];
                addFoodToTextView();
            }
        });

    }

    private List<Food> getFoodList(){
        List<Food> food;
        if(currentLocation.equals("All")){
            if(currentFilter.equals("date_added")){
                food = MainMenuPlaceholder.database.foodDAO().getFood();
            }else if(currentFilter.equals("expiryDate")){
                food = MainMenuPlaceholder.database.foodDAO().getAllByExpiry();
            }else if(currentFilter.equals("category")){
                food = MainMenuPlaceholder.database.foodDAO().getAllByCategory();
            }else if(currentFilter.equals("name")){
                food = MainMenuPlaceholder.database.foodDAO().getAllByName();
            }else if(currentFilter.equals("quantity")){
                food = MainMenuPlaceholder.database.foodDAO().getAllByQuantity();
            }else{
                food = new ArrayList<>();
                System.err.println(">>>Something went wrong, Filter: " + currentFilter + " and Location: " + currentLocation + " mix badly");
            }
        }else if(currentFilter.equals("date_added")){
            food = MainMenuPlaceholder.database.foodDAO().getFoodByAdded(currentLocation);
        }else if(currentFilter.equals("expiryDate")){
            food = MainMenuPlaceholder.database.foodDAO().getFoodByExpiry(currentLocation);
        }else if(currentFilter.equals("category")){
            food = MainMenuPlaceholder.database.foodDAO().getFoodByCategory(currentLocation);
        }else if(currentFilter.equals("name")){
            food = MainMenuPlaceholder.database.foodDAO().getFoodByName(currentLocation);
        }else if(currentFilter.equals("quantity")){
            food = MainMenuPlaceholder.database.foodDAO().getFoodByQuantity(currentLocation);
        }else{
            food = new ArrayList<>();
            System.err.println(">>>Something went wrong, Filter: " + currentFilter + " and Location: " + currentLocation + " mix badly");
        }
        return food;
    }

    private void addFoodToTextView(){
        List<Food> food = getFoodList();
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