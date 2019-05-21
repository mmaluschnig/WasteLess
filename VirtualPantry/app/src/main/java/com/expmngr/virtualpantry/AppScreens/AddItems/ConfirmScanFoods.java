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

    private Map<String,Long> timeDict = new HashMap<String,Long>(){
        {
            Long day = new Long(1000*60*60*24);
            put("day",day);
            put("week",day * 7);
            put("month", day * 30);
            put("year", day * 365);
        }
    };

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
        for(Food f : currentFoodsToAdd){
            MainMenuPlaceholder.database.foodDAO().addFood(f);
        }
        Toast.makeText(ConfirmScanFoods.this, "Added Foods", Toast.LENGTH_SHORT).show();
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
            public List<ExpiryFood> onItemClick(int position) {
                ScannedFood selectedFood = (ScannedFood)food.get(position);
                System.out.println("SCanned string " + selectedFood.getScannedString());
                return foodOptions.get(selectedFood.getScannedString());
            }

            @Override
            public void onExpiryFoodChange(int position, int index){
                ScannedFood selectedFood = (ScannedFood)food.get(position);
                List<ExpiryFood> options = foodOptions.get(selectedFood.getScannedString());
                ScannedFood scannedFood = expToScannedFood(options.get(index), selectedFood.getScannedString());
                System.out.println("clicked: " + options.get(index).getName() + " got " + scannedFood.getName());

                food.set(position, scannedFood);

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
                                food.remove(position);
                                adapter.notifyItemRemoved(position);
                                Toast.makeText(ConfirmScanFoods.this, "Deleted: " + name, Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            }

            @Override
            public void onLocationChange(int position, String newLocation, TextView dateText){
                ScannedFood sFood = (ScannedFood) food.get(position);
                String expiry = sFood.getDateByLocation(newLocation);

                dateText.setText(expiry.subSequence(0,10));
            }

            @Override
            public void onConfirmEditClick(int position, Food editFood) {
                food.get(position).setName(editFood.getName());
                food.get(position).setCategory(editFood.getCategory());
                food.get(position).setQuantity(editFood.getQuantity());
                food.get(position).setLocation(editFood.getLocation());
                food.get(position).setExpiryDate(editFood.getExpiryDate());
                //System.out.println("EXP: " + food.get(position).getExpiryDate());

                //adapter.notifyItemChanged(position);
            }

        });
    }

    private void getInitialFoods(){
        Iterator iterator = foodOptions.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry pair = (Map.Entry)iterator.next();
            List<ExpiryFood> options = (List<ExpiryFood>) pair.getValue();
            String scannedName = (String) pair.getKey();
            if(options.size() > 0) {
                ExpiryFood expiryFood = options.get(0);

                ScannedFood scannedFood = expToScannedFood(expiryFood, scannedName);


                currentFoodsToAdd.add(scannedFood);
            }

            //iterator.remove();
        }
    }

    private ScannedFood expToScannedFood(ExpiryFood expiryFood, String scannedName){
        ScannedFood scannedFood = new ScannedFood();
        scannedFood.setFromFood(expToFood(expiryFood));
        scannedFood.setScannedString(scannedName);


        scannedFood.addDate("Pantry", timeToDate(expiryFood.getPantryExpiry()));
        scannedFood.addDate("Fridge", timeToDate(expiryFood.getFridgeExpiry()));
        scannedFood.addDate("Freezer", timeToDate(expiryFood.getFreezerExpiry()));
        String now = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        if(!scannedFood.getDateByLocation("Pantry").contains(now)){
            System.out.println("PANTRY");
            scannedFood.setExpiryDate(scannedFood.getDateByLocation("Pantry"));
            scannedFood.setLocation("Pantry");
        }else if(!scannedFood.getDateByLocation("Fridge").contains(now)){
            System.out.println("FRIDGE");
            scannedFood.setExpiryDate(scannedFood.getDateByLocation("Fridge"));
            scannedFood.setLocation("Fridge");
        }else if(!scannedFood.getDateByLocation("Freezer").contains(now)){
            System.out.println("FREEZER");
            scannedFood.setExpiryDate(scannedFood.getDateByLocation("Freezer"));
            scannedFood.setLocation("Freezer");
        }else {
            System.out.println("PANTRY");
            scannedFood.setExpiryDate(scannedFood.getDateByLocation("Pantry"));
            scannedFood.setLocation("Pantry");
        }
        return scannedFood;
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
        Date now = new Date();

        if(time.equals("")){
            System.out.println("NULL");
            return new SimpleDateFormat("dd/MM/yyyy HH").format(now);
        }

        Double num = getNumberFromTime(time);

        System.out.println("Time: " + time);
        if(time.toLowerCase().contains("day")){
            System.out.println("DAY");
            return getDateFromTime("day", num);
        }else if(time.toLowerCase().contains("week")){
            System.out.println("WEEK");
            return getDateFromTime("week", num);
        }else if(time.toLowerCase().contains("month")){
            System.out.println("MONTH");
            return getDateFromTime("month", num);
        }else if(time.toLowerCase().contains("years")){
            System.out.println("YEAR");
            return getDateFromTime("year", num);
        }else {
            //weird words
            System.out.println("BAD: " + time);
        }
        return new SimpleDateFormat("dd/MM/yyyy HH").format(now);
    }

    private Double getNumberFromTime(String time){
        if(time.contains("-")){
            String[] numbers = time.toLowerCase().replaceAll("[A-Za-z ]*","").split("-");

            Integer total = Integer.parseInt(numbers[0]) + Integer.parseInt(numbers[1]);
            Double average = total.doubleValue() / 2;
            return average;
        }else if(time.matches(".*\\d.*")){
            String number = time.toLowerCase().replaceAll("\\D","");
            System.out.println("REGEX" + number);
            return Double.parseDouble(number);
        }else{
            return 0d;
        }
    }

    private String getDateFromTime(String timeFrame, Double multiplier){

        Date now = new Date();
        Double extraTime = (multiplier * timeDict.get(timeFrame));
        Date expiry = new Date(now.getTime() + extraTime.longValue());
        return new SimpleDateFormat("dd/MM/yyyy HH").format(expiry);
    }

    private void setUpButtons(){
        Button addScannedFoodButton = (Button) findViewById(R.id.addScannedFoodsButton);
        addScannedFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addScannedItemsToPantry();

                startActivity(
                        new Intent(getApplicationContext(), MainMenuPlaceholder.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK)
                );
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
