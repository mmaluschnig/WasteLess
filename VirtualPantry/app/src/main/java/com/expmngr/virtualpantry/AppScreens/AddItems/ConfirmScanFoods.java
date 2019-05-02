package com.expmngr.virtualpantry.AppScreens.AddItems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.expmngr.virtualpantry.AppScreens.MainMenuPlaceholder;
import com.expmngr.virtualpantry.Database.Entities.ExpiryFood;
import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmScanFoods extends AppCompatActivity {
    TextView confirmFoodTextView;

    private Map<String,List<ExpiryFood>> foodOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_scan_foods);

        foodOptions = new HashMap<String, List<ExpiryFood>>();

        confirmFoodTextView = (TextView) findViewById(R.id.confirmFoodTextView);

        List<String> foundWords = getIntent().getStringArrayListExtra("found_foods");

        String totalFoodInfo = "";
        if(foundWords.size() > 0) {
            for (String s : foundWords) {
                List<ExpiryFood> foundFood = MainMenuPlaceholder.database.expiryFoodDAO().findByName("%" + s + "%");
                foodOptions.put(s, foundFood);

                String foodInfo = "Scanned: " + s + ", Found:\n----------------------------------\n";
                for (ExpiryFood f : foundFood) {
                    foodInfo += "Name:\t\t\t\t" + f.getName() +
                            "\nCategory:\t\t" + f.getCat_num() +
                            "\nStatus:\t\t\t\t" + f.getStatus() +
                            "\nPantry:\t\t\t\t" + f.getPantryExpiry() +
                            "\nFridge:\t\t\t\t" + f.getFridgeExpiry() +
                            "\nFreezer:\t\t\t" + f.getFreezerExpiry() + "\n\n";
                }
                totalFoodInfo += foodInfo + "\n";
            }
        }else{
            totalFoodInfo = "Sorry, no food was found. Try scanning again!";
        }

//        String info = "";
//
//        for(Food f : food) {
//            int id = f.getId();
//            String name = f.getName();
//            float quantity = f.getQuantity();
//            String expDate = f.getExpiryDate();
//            String addedDate = f.getDate_added();
//
//            info = info + "\nID : " + id + "\nName : " + name + "\nLocation :" + loc + "\nQuantity : " + quantity + "\nExpires on: " + expDate + " (" + "N/A" + " hours)\nAdded: " + addedDate + " (" + "N/A" + " hours old)\n ";
//        }

        confirmFoodTextView.setText(totalFoodInfo);
    }
}
