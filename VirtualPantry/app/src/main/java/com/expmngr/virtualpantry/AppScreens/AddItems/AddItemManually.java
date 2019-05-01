package com.expmngr.virtualpantry.AppScreens.AddItems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.expmngr.virtualpantry.AppScreens.AddNewItems;
import com.expmngr.virtualpantry.AppScreens.MainMenuPlaceholder;
import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.MainActivity;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.BottomNavigationViewHelper;
import com.expmngr.virtualpantry.test1;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemManually extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 1;
    EditText foodNameText;
    EditText foodCategorytext;
    EditText foodQuantityText;
    Spinner foodLocationSpinner;
    EditText foodTimeTillExpiryText;

    private String[] locations = {"Pantry", "Fridge", "Freezer"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_manually);

        setUpInputFields();
        setupBottomNavigationView();


        Button addfoodManuallyButton = (Button) findViewById(R.id.addManualFoodButton);
        addfoodManuallyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Food newFood = new Food();
                newFood.setName(foodNameText.getText().toString());
                newFood.setCategory(foodCategorytext.getText().toString());
                newFood.setQuantity(Float.parseFloat(foodQuantityText.getText().toString()));
                newFood.setLocation(foodLocationSpinner.getSelectedItem().toString());
                newFood.setDate_added(new SimpleDateFormat("dd/MM/yyyy HH").format(new Date()));
                newFood.setExpiryDate(foodTimeTillExpiryText.getText().toString() + " 00");
                try {
                    MainMenuPlaceholder.database.foodDAO().addFood(newFood);
                }finally {
                    foodNameText.setText("");
                    foodCategorytext.setText("");
                    foodQuantityText.setText("");
                    foodTimeTillExpiryText.setText("");
                    foodLocationSpinner.setSelection(0);
                    Toast.makeText(AddItemManually.this, "Added: " + newFood.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button deletaAllButton = (Button) findViewById(R.id.deleteAllButton);
        deletaAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuPlaceholder.database.foodDAO().deleteAll();
                Toast.makeText(AddItemManually.this, "Deleted All", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUpInputFields(){
        foodNameText = (EditText) findViewById(R.id.foodNameEditText);
        foodCategorytext = (EditText) findViewById(R.id.foodCategoryEditText);
        foodQuantityText = (EditText) findViewById(R.id.foodQuantityEditText);
        foodTimeTillExpiryText = (EditText) findViewById(R.id.foodTimeTillExpiryEditText);

        foodLocationSpinner = (Spinner) findViewById(R.id.foodLocationSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.food_locations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodLocationSpinner.setAdapter(adapter);


    }

    private void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigationViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(AddItemManually.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
