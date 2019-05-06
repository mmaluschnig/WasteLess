package com.expmngr.virtualpantry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.expmngr.virtualpantry.AppScreens.MainMenuPlaceholder;
import com.expmngr.virtualpantry.Database.Entities.ExpiryFood;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class readCSVFiles extends AppCompatActivity {
    Map<String,String> keywordDict;

    TextView displayText;

    //columns of csv file
    final int NAME = 0;
    final int STATUS = 1;
    final int PANTRY = 2;
    final int FRIDGE = 3;
    final int FREEZER = 4;
    final int GROUP = 6;//intentional skip 4-6
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_csvfiles);

        displayText = (TextView) findViewById(R.id.csvTextView);

        MainActivity.database.expiryFoodDAO().deleteAll();

        addExpiryTimes();
        addKeywords();
        displayExpiryFood();


    }

    private void displayExpiryFood(){
        //display food in expdate entity
        List<ExpiryFood> expFood = MainActivity.database.expiryFoodDAO().getAllExpFood();
        String info = "";

        for(ExpiryFood f : expFood) {
            int id = f.getId();
            String name = f.getName();
            String status = f.getStatus();
            String pantry = f.getPantryExpiry();
            String fridge = f.getFridgeExpiry();
            String freezer = f.getFreezerExpiry();

            info = info + "\nID : " + id + "\nName : " + name + "\nStatus :" + status+ "\nPantry : " + pantry + "\nFridge : " + fridge + "\nFreezer: " + freezer + "\n";
        }
        displayText.setText(info);
    }

    private void addExpiryTimes(){
        ArrayList<String[]> myValues = csvToArrayBetter(R.raw.expiry_times);
        System.out.println(Arrays.deepToString(myValues.toArray()));
        List<ExpiryFood> expFoods = new ArrayList<>();
        for(String[] line : myValues){
            for(String s : line){
                System.out.print(s +";");
            }
            ExpiryFood newFood = new ExpiryFood();
            newFood.setName(line[NAME]);
            newFood.setStatus(line[STATUS]);
            newFood.setPantryExpiry(line[PANTRY]);
            newFood.setFridgeExpiry(line[FRIDGE]);
            newFood.setFreezerExpiry(line[FREEZER]);
            //newFood.setCat_num(Integer.parseInt(line[GROUP]));
            MainActivity.database.expiryFoodDAO().addExpFood(newFood);

            System.out.println();
        }
    }

    private void addKeywords(){
        ArrayList<String[]> myValues = csvToArrayBetter(R.raw.keywords);
        System.out.println(Arrays.deepToString(myValues.toArray()));

        keywordDict = new HashMap<>();

        for(String[] line : myValues){
            //every word in the expiry times needs a key pointing to itself
            keywordDict.put(line[0],line[0]);

            if(!line[1].equals("")){
                //words that are similar to a word in expiry times point to that word
                keywordDict.put(line[1],line[0]);
            }
        }

        System.out.println(keywordDict.toString());
    }


    private ArrayList<String[]> csvToArrayBetter(int resource) {

        InputStream is = getResources().openRawResource(resource);

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


    private ArrayList<String[]> csvWithQuotesToArray(){

        InputStream is = getResources().openRawResource(R.raw.expiry_times);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";
        ArrayList<String[]> lines = new ArrayList<>();
        ArrayList<String> temparray = new ArrayList<>();
        int pointer = 0;
        int count = 0;

        Boolean inQuotes = false;

        try{
            //row titles
            line = reader.readLine();

            line = reader.readLine();
            while(line != null){
                inQuotes = false;
                count = 0;
                pointer = 0;
                temparray = new ArrayList<>();
                for(char c : line.toCharArray()){
                    if(c == '"'){
                        inQuotes = !inQuotes;
                        if(!inQuotes){
                            temparray.add(line.substring(pointer + 1,count));
                            //System.out.println("QUOTE : " + line.substring(pointer + 1,count));
                            pointer = count + 1;
                        }

                    }else if(c == ',' && !inQuotes){
                        temparray.add(line.substring(pointer,count));
                        //System.out.println("COMMA " + line.substring(pointer,count));
                        pointer = count + 1;
                    }
                    count++;
                }
                temparray.add(line.substring(pointer, count));
                //System.out.println("END: " + line.substring(pointer,count));

                String[] temp = new String[temparray.size()];
                temp = temparray.toArray(temp);
                lines.add(temp);

                //lines.add(line.split(",", 2));

                line = reader.readLine();
            }
            System.out.println("Array");
            System.out.println(Arrays.deepToString(lines.toArray()));
//                    for(int i = 0; i < lines.size();i++){
//                        String[] x = lines.get(i);
//
//                        }
//                        System.out.println("\n");
//                    }

        }catch (IOException e){
            e.printStackTrace();
        }
        return lines;
    }
}
