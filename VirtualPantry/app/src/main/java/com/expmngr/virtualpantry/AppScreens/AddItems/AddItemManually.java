package com.expmngr.virtualpantry.AppScreens.AddItems;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.expmngr.virtualpantry.AppScreens.MainMenuPlaceholder;
import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.BottomNavigationViewHelper;
import com.expmngr.virtualpantry.Utils.SettingsVariables;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemManually extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 1;
    EditText foodNameText;
    AutoCompleteTextView foodCategorytext;
    EditText foodQuantityText;
    Spinner foodLocationSpinner;
    EditText foodTimeTillExpiryText;

    private String[] locations = {"Pantry", "Fridge", "Freezer"};
    final Calendar myCalendar = Calendar.getInstance();

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
                String name = foodNameText.getText().toString();
                String category = foodCategorytext.getText().toString();
                Float quantity = 0f;
                String quantityMessage = "";
                try {
                    quantity = Float.parseFloat(foodQuantityText.getText().toString());

                }catch (Exception e){
                    quantityMessage = e.getMessage();
                }
                String location = foodLocationSpinner.getSelectedItem().toString();
                String addedDate = new SimpleDateFormat("dd/MM/yyyy HH").format(new Date());
                String expiryDate = foodTimeTillExpiryText.getText().toString() + SettingsVariables.expirytime;

                //Check that fields are correct
                if(name.equals("")){
                    Toast.makeText(AddItemManually.this, "Food must have a Name", Toast.LENGTH_SHORT).show();
                }else if(!quantityMessage.equals("")){
                    Toast.makeText(AddItemManually.this, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                } else if(quantity <= 0){
                    Toast.makeText(AddItemManually.this, "Quantity must be greater that 0", Toast.LENGTH_SHORT).show();
                }else if(category.equals("")){
                    Toast.makeText(AddItemManually.this, "Food must have a category", Toast.LENGTH_SHORT).show();
                }else if(expiryDate.equals(SettingsVariables.expirytime)){
                    Toast.makeText(AddItemManually.this, "Food must have an expiry date", Toast.LENGTH_SHORT).show();
                }else {
                    //everything verifies
                    Food newFood = new Food();
                    newFood.setName(name);
                    newFood.setCategory(category);
                    newFood.setQuantity(quantity);
                    newFood.setLocation(location);
                    newFood.setDate_added(addedDate);
                    newFood.setExpiryDate(expiryDate);
                    try {
                        MainMenuPlaceholder.database.foodDAO().addFood(newFood);
                    } finally {
                        foodNameText.setText("");
                        foodCategorytext.setText("");
                        foodQuantityText.setText("");
                        foodTimeTillExpiryText.setText("");
                        foodLocationSpinner.setSelection(0);
                        Toast.makeText(AddItemManually.this, "Added: " + newFood.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//        Button deletaAllButton = (Button) findViewById(R.id.deleteAllButton);
//        deletaAllButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainMenuPlaceholder.database.foodDAO().deleteAll();
//                Toast.makeText(AddItemManually.this, "Deleted All", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void setUpInputFields(){
        foodNameText = (EditText) findViewById(R.id.foodNameEditText);

        foodCategorytext = (AutoCompleteTextView) findViewById(R.id.foodCategoryEditText);
        String[] categories = getResources().getStringArray(R.array.food_categories);
        ArrayAdapter<String> cat_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
        foodCategorytext.setAdapter(cat_adapter);

        foodQuantityText = (EditText) findViewById(R.id.foodQuantityEditText);

        setUpExpiryDateInput();

        foodLocationSpinner = (Spinner) findViewById(R.id.foodLocationSpinner);
        ArrayAdapter<CharSequence> loc_adapter = ArrayAdapter.createFromResource(this, R.array.food_locations, android.R.layout.simple_spinner_item);
        loc_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodLocationSpinner.setAdapter(loc_adapter);


    }

    private void setUpExpiryDateInput(){
        foodTimeTillExpiryText = (EditText) findViewById(R.id.foodTimeTillExpiryEditText);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                foodTimeTillExpiryText.setText(sdf.format(myCalendar.getTime()));
            }
        };

        foodTimeTillExpiryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddItemManually.this, android.R.style.Theme_DeviceDefault_Dialog_Alert, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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
