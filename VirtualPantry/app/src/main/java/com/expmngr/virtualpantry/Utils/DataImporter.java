package com.expmngr.virtualpantry.Utils;

import android.content.Context;

import com.expmngr.virtualpantry.AppScreens.MainMenuPlaceholder;
import com.expmngr.virtualpantry.Database.Entities.ExpiryFood;
import com.expmngr.virtualpantry.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataImporter {
    Context context;

    //columns of csv file
    final int NAME = 0;
    final int STATUS = 1;
    final int PANTRY = 2;
    final int FRIDGE = 3;
    final int FREEZER = 4;
    final int GROUP = 6;//intentional skip 4-6

    public DataImporter(Context context) {
        this.context = context;
    }

    public void addExpiryTimes() {
        ArrayList<String[]> myValues = csvToArrayBetter(R.raw.expiry_times);
        //System.out.println(Arrays.deepToString(myValues.toArray()));
        List<ExpiryFood> expFoods = new ArrayList<>();
        for (String[] line : myValues) {
            ExpiryFood newFood = new ExpiryFood();
            newFood.setName(line[NAME]);
            newFood.setStatus(line[STATUS]);
            newFood.setPantryExpiry(line[PANTRY]);
            newFood.setFridgeExpiry(line[FRIDGE]);
            newFood.setFreezerExpiry(line[FREEZER]);
            //newFood.setCat_num(Integer.parseInt(line[GROUP]));
            MainMenuPlaceholder.database.expiryFoodDAO().addExpFood(newFood);
        }
    }

    private ArrayList<String[]> csvToArrayBetter(int resource) {

        InputStream is = context.getResources().openRawResource(resource);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";
        ArrayList<String[]> lines = new ArrayList<>();

        try {
            //row titles
            line = reader.readLine();

            line = reader.readLine();
            while(line != null) {
                lines.add(line.split(","));
                line = reader.readLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return lines;
    }
}
