package com.expmngr.virtualpantry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.expmngr.virtualpantry.Database.Entities.Food;

public class test extends AppCompatActivity {
    EditText nameInput;
    EditText quantityInput;
    EditText expiryInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        nameInput = (EditText) findViewById(R.id.nameInput);
        quantityInput = (EditText) findViewById(R.id.quantityInput);
        expiryInput = (EditText) findViewById(R.id.expiryInput);

        Button viewList = (Button) findViewById(R.id.viewList);
        viewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),test1.class);
                startActivity(intent);

            }
        });

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food newFood = new Food();
                newFood.setName(nameInput.getText().toString());
                newFood.setQuantity(Integer.parseInt(quantityInput.getText().toString()));
                //newFood.setTime_till_expiry(new Integer("10"));

                MainActivity.database.foodDAO().addFood(newFood);
                Toast.makeText(test.this, "Successful add", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
