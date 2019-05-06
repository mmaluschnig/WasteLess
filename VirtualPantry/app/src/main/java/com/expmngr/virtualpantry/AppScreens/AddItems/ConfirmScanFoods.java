package com.expmngr.virtualpantry.AppScreens.AddItems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.expmngr.virtualpantry.AppScreens.MainMenuPlaceholder;
import com.expmngr.virtualpantry.Database.Entities.ExpiryFood;
import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfirmScanFoods extends AppCompatActivity {
    TextView confirmFoodTextView;

    private Map<String,List<ExpiryFood>> foodOptions;
    private HashMap<String, ArrayList<String>> keywordDict;
    Set<String> blackList; //improves efficiency by remembering words that are not in the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_scan_foods);

        foodOptions = new HashMap<String, List<ExpiryFood>>();
        blackList = new HashSet<>(Arrays.asList("eat"));

        confirmFoodTextView = (TextView) findViewById(R.id.confirmFoodTextView);

        List<String> foundWords = getIntent().getStringArrayListExtra("found_foods");
        keywordDict = (HashMap<String, ArrayList<String>>) getIntent().getSerializableExtra("keywords");
        System.out.println("keywords" + keywordDict.toString());
        String totalFoodInfo = "";
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

                    String foodInfo = "Scanned: " + s + ", Found:\n----------------------------------\n";
                    for (ExpiryFood f : found) {
                        foodInfo += "Name:\t\t\t\t" + f.getName() +
                                "\nCategory:\t\t" + f.getCat_num() +
                                "\nStatus:\t\t\t\t" + f.getStatus() +
                                "\nPantry:\t\t\t\t" + f.getPantryExpiry() +
                                "\nFridge:\t\t\t\t" + f.getFridgeExpiry() +
                                "\nFreezer:\t\t\t" + f.getFreezerExpiry() + "\n\n";
                    }
                    totalFoodInfo += foodInfo + "\n";
                }
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
