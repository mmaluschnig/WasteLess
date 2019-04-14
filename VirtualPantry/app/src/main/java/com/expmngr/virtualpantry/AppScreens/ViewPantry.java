package com.expmngr.virtualpantry.AppScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.MainActivity;
import com.expmngr.virtualpantry.R;

import java.util.List;

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

            info = info + "\n ID : " + id + "\n Name : " + name + "\n Quantity : " + quantity + "\n Expires on: " + expDate + " \n";
        }
        pantryItemsTextView.setText(info);
    }
}