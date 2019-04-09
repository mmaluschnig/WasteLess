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
            Integer time2exp = f.getTime_till_expiry();

            info = info + "\n ID : " + id + "\n Name : " + name + "\n Quantity : " + quantity + "\n Expires in: " + time2exp + " hours\n";
        }
        pantryItemsTextView.setText(info);
    }
}