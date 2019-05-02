package com.expmngr.virtualpantry.AppScreens;

import android.os.Bundle;
import android.widget.TextView;

import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.R;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class ShoppingList extends AppCompatActivity {
    TextView shoppingListTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist);

        shoppingListTextView = (TextView) findViewById(R.id.shoppingListTextView);


        List<Food> expiredfood = MainMenuPlaceholder.database.foodDAO().getExpiredFood();

        String info = "";

        for(Food f: expiredfood){

            int id=f.getId();
            String name=f.getName();
            Boolean hasExpired = f.getIsExpired();
            String expDate = f.getExpiryDate();
            String addedDate = f.getDate_added();



            info += name +"\n\n";
            //String test = "this is a test\n\n\n\nwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww\n\n\nwwwwwwwwwww";
            System.out.println("here");


        }
        shoppingListTextView.setText(info);
    }
}

