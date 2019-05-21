package com.expmngr.virtualpantry.AppScreens;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expmngr.virtualpantry.Database.Entities.ExpiryFood;
import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.Database.Entities.FoodGroup;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.BottomNavigationViewHelper;
import com.expmngr.virtualpantry.Utils.SettingsVariables;
import com.expmngr.virtualpantry.Utils.SimpleImageArrayAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewPantry extends AppCompatActivity {

    Spinner filterSpinner;
    RecyclerView rvFood;

    final Calendar myCalendar = Calendar.getInstance();

    private String currentLocation;
    private String currentFilter;
    private String[] filters = {"date_added", "expiryDate", "category", "name", "quantity"};
    
    private static final int ACTIVITY_NUM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pantry);
        //prevents on screen keyboard from popping up untill user clicks on editText
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setupBottomNavigationView();
        setUpButtons();

        rvFood = findViewById(R.id.rvFood);
        rvFood.addItemDecoration(new DividerItemDecoration(rvFood.getContext(), DividerItemDecoration.VERTICAL));

        currentLocation = "All";
        currentFilter = filters[0];
        updateRecyclerView(getFoodList());

    }

    private void setUpButtons(){

        Button viewAllButton = (Button) findViewById(R.id.viewAllButton);
        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentLocation = "All";
                updateRecyclerView(getFoodList());

            }
        });

        Button filterByPantry = (Button) findViewById(R.id.pantryButton);
        filterByPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentLocation = "Pantry";
                updateRecyclerView(getFoodList());
            }
        });

        Button filterByFridge = (Button) findViewById(R.id.fridgeButton);
        filterByFridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentLocation = "Fridge";
                updateRecyclerView(getFoodList());
            }
        });

        Button filterByFreezer = (Button) findViewById(R.id.freezerButton);
        filterByFreezer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentLocation = "Freezer";
                updateRecyclerView(getFoodList());
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
                updateRecyclerView(getFoodList());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentFilter = filters[0];
                updateRecyclerView(getFoodList());
            }
        });

    }

    private void updateRecyclerView(final List<Food> food){
        if (MainMenuPlaceholder.database.foodDAO().getFood() == null){
            //TODO notify empty pantry
            return;

        }
        final FoodAdapter adapter = new FoodAdapter(food);
        rvFood.setAdapter(adapter);
        rvFood.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onSetup(final TextView dateTextView, final Spinner locationSpinner) {
                //SETUP DATE
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                        dateTextView.setText(sdf.format(myCalendar.getTime()));
                    }
                };

                dateTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(ViewPantry.this, android.R.style.Theme_DeviceDefault_Dialog_Alert, date,
                                myCalendar.get(Calendar.YEAR),
                                myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                //SETUP LOCATION
                SimpleImageArrayAdapter adapter = new SimpleImageArrayAdapter(ViewPantry.this,
                        new Integer[]{R.drawable.ic_pantry,R.drawable.ic_food_and_restaurant,R.drawable.ic_snow});
                locationSpinner.setAdapter(adapter);
            }


            @Override
            public List<ExpiryFood> onItemClick(int position) {
                return null;
            }

            @Override
            public void onExpiryFoodChange(int position, int index){

            }

            @Override
            public void onDeleteClick(final int position) {

                new AlertDialog.Builder(ViewPantry.this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                        .setTitle("Confirm Delete")
                        .setMessage("Are your sure you want to delete " + food.get(position).getName() + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                String name = food.get(position).getName();
                                MainMenuPlaceholder.database.foodDAO().deleteFood(food.get(position));
                                food.remove(position);
                                adapter.notifyItemRemoved(position);
                                Toast.makeText(ViewPantry.this, "Deleted" + name, Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null)
                        .show();

//
            }

            @Override
            public void onLocationChange(int position, String newLocation, TextView dateText){

            }

            @Override
            public void onConfirmEditClick(int position, Food editFood) {
                food.get(position).setName(editFood.getName());
                food.get(position).setCategory(editFood.getCategory());
                food.get(position).setQuantity(editFood.getQuantity());
                food.get(position).setLocation(editFood.getLocation());
                food.get(position).setExpiryDate(editFood.getExpiryDate());

                MainMenuPlaceholder.database.foodDAO().updateFood(food.get(position));

                adapter.notifyItemChanged(position);
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