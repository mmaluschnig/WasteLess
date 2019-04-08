package com.expmngr.virtualpantry.AppScreens.AddItems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.expmngr.virtualpantry.AppScreens.MainMenuPlaceholder;
import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.MainActivity;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.test1;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemManually extends AppCompatActivity {
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

        Button addfoodManuallyButton = (Button) findViewById(R.id.addManualFoodButton);
        addfoodManuallyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food newFood = new Food();
                newFood.setName(foodNameText.getText().toString());
                newFood.setCategory(foodCategorytext.getText().toString());
                newFood.setQuantity(Integer.parseInt(foodQuantityText.getText().toString()));
                newFood.setLocation(foodLocationSpinner.getSelectedItem().toString());
                newFood.setDate_added(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                newFood.setTime_till_expiry(Integer.parseInt(foodTimeTillExpiryText.getText().toString()));

                MainMenuPlaceholder.database.foodDAO().addFood(newFood);
                Toast.makeText(AddItemManually.this, "Successful add", Toast.LENGTH_SHORT).show();
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
}
