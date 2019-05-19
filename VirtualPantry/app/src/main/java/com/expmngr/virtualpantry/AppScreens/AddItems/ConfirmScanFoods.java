package com.expmngr.virtualpantry.AppScreens.AddItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expmngr.virtualpantry.AppScreens.FoodAdapter;
import com.expmngr.virtualpantry.AppScreens.MainMenuPlaceholder;
import com.expmngr.virtualpantry.AppScreens.ShoppingList;
import com.expmngr.virtualpantry.AppScreens.ViewPantry;
import com.expmngr.virtualpantry.Database.Entities.ExpiryFood;
import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.Database.Entities.ScannedFood;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.BottomNavigationViewHelper;
import com.expmngr.virtualpantry.Utils.SettingsVariables;
import com.expmngr.virtualpantry.Utils.SimpleImageArrayAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfirmScanFoods extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 1;

    final Calendar myCalendar = Calendar.getInstance();
    RecyclerView rvFood;

    private Map<String,List<ExpiryFood>> foodOptions;
    private HashMap<String, ArrayList<String>> keywordDict;
    Set<String> blackList; //improves efficiency by remembering words that are not in the database

    private List<Food> currentFoodsToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_scan_foods);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        foodOptions = new HashMap<String, List<ExpiryFood>>();
        blackList = new HashSet<>(Arrays.asList("eat"));
        currentFoodsToAdd = new ArrayList<>();

        setupBottomNavigationView();
        setUpButtons();

        rvFood = findViewById(R.id.confirmFoodRV);
        rvFood.addItemDecoration(new DividerItemDecoration(rvFood.getContext(), DividerItemDecoration.VERTICAL));

        findFoods();
        getInitialFoods();
        updateRecyclerView(currentFoodsToAdd);

    }

    private void addScannedItemsToPantry(){
        //TODO Add scanned food to pantry
    }

    private void findFoods(){
        List<String> foundWords = getIntent().getStringArrayListExtra("found_foods");
        keywordDict = (HashMap<String, ArrayList<String>>) getIntent().getSerializableExtra("keywords");
        if(foundWords.size() > 0) {
            //check potential foods against the database
            for(String s : foundWords){
                if(!blackList.contains(s)) {
                    List<ExpiryFood> found = new ArrayList<>();
                    ArrayList<String> keyword = keywordDict.get(s);
                    if(keyword != null) {
                        for (String value : keyword) {
                            found.addAll(MainMenuPlaceholder.database.expiryFoodDAO().findByName("%" + value + "%"));
                        }
                    }else{
                        found.addAll(MainMenuPlaceholder.database.expiryFoodDAO().findByName("%" + keyword + "%"));
                    }
                    //TODO remove duplicate foods here (or find out why theyre being duplicated)
                    if (found.size() > 0) {
                        //one of our potential foods was a keyword!
                        foodOptions.put(s, found);
                    }else{
                        found = MainMenuPlaceholder.database.expiryFoodDAO().findByName("%" + s + "%");
                        if (found.size() > 0) {
                            //one of our potential foods was in the database!
                            foodOptions.put(s, found);
                        }else {
                            blackList.add(s);
                        }
                    }
                }
            }
        }else{
            System.out.println("Sorry, no food was found. Try scanning again!");
        }
    }

    private void updateRecyclerView(final List<Food> food){
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
                        new DatePickerDialog(ConfirmScanFoods.this, android.R.style.Theme_DeviceDefault_Dialog_Alert, date,
                                myCalendar.get(Calendar.YEAR),
                                myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                //SETUP LOCATION
                SimpleImageArrayAdapter adapter = new SimpleImageArrayAdapter(ConfirmScanFoods.this,
                        new Integer[]{R.drawable.ic_pantry,R.drawable.ic_food_and_restaurant,R.drawable.ic_snow});
                locationSpinner.setAdapter(adapter);
            }


            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(getApplicationContext(), ShoppingList.class));
                food.get(position).setName("Clicked");
                adapter.notifyItemRemoved(position);
                adapter.notifyItemChanged(position);

            }

            @Override
            public void onDeleteClick(final int position) {

                new AlertDialog.Builder(ConfirmScanFoods.this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                        .setTitle("Confirm Delete")
                        .setMessage("Are your sure you want to delete " + food.get(position).getName() + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                String name = food.get(position).getName();
                                MainMenuPlaceholder.database.foodDAO().deleteFood(food.get(position));
                                food.remove(position);
                                adapter.notifyItemRemoved(position);
                                Toast.makeText(ConfirmScanFoods.this, "Deleted" + name, Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null)
                        .show();

//
            }

            @Override
            public void onConfirmEditClick(int position, Food editFood) {
                food.get(position).setName(editFood.getName());
                food.get(position).setCategory(editFood.getCategory());
                food.get(position).setQuantity(editFood.getQuantity());
                food.get(position).setLocation(editFood.getLocation());
                //if expiry date hasnt been edited chnge the expiry date to the current locations exp date
                if(food.get(position).getExpiryDate().equals(editFood.getExpiryDate())){
                    ScannedFood sFood = (ScannedFood) food.get(position);
                    food.get(position).setExpiryDate(sFood.getDateByLocation(editFood.getLocation()));
                }else {
                    food.get(position).setExpiryDate(editFood.getExpiryDate());
                }

                MainMenuPlaceholder.database.foodDAO().updateFood(food.get(position));
            }

        });
    }

    private void getInitialFoods(){
        Iterator iterator = foodOptions.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry pair = (Map.Entry)iterator.next();
            List<ExpiryFood> options = (List<ExpiryFood>) pair.getValue();
            if(options.size() > 0) {
                ExpiryFood expiryFood = options.get(0);

                ScannedFood scannedFood = new ScannedFood();
                scannedFood.setFromFood(expToFood(expiryFood));


                scannedFood.addDate("Pantry", timeToDate(expiryFood.getPantryExpiry()));
                scannedFood.addDate("Fridge", timeToDate(expiryFood.getFridgeExpiry()));
                scannedFood.addDate("Freezer", timeToDate(expiryFood.getFreezerExpiry()));
                scannedFood.setExpiryDate(scannedFood.getDateByLocation("Pantry"));
                currentFoodsToAdd.add(scannedFood);
            }

            iterator.remove();
        }
        System.out.println(foodOptions.toString());
    }

    private Food expToFood(ExpiryFood expFood){
        Food food = new Food();
        food.setName(expFood.getName());
        food.setCategory(expFood.getCategory());
        food.setQuantity(1f);
        food.setLocation("Pantry");

        return food;
    }

    private String timeToDate(String time){
        //TODO convert from '1 day' or '3 weeks' to date
        Date now = new Date();

        return new SimpleDateFormat("dd/MM/yyyy HH").format(new Date());
    }

    private void setUpButtons(){
        Button addScannedFoodButton = (Button) findViewById(R.id.addScannedFoodsButton);
        addScannedFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addScannedItemsToPantry();
                startActivity(new Intent(getApplicationContext(), ViewPantry.class));
            }
        });

        Button keepScanningButton = (Button) findViewById(R.id.keepScanningButton);
        keepScanningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigationViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(ConfirmScanFoods.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
