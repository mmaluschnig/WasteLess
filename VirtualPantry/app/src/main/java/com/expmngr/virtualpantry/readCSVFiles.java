package com.expmngr.virtualpantry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

public class readCSVFiles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_csvfiles);

        ArrayList<String[]> myValues = csvToArray();
    }

    private ArrayList<String[]> csvToArray(){

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
