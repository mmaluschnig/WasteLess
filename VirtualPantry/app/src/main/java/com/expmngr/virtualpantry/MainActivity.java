package com.expmngr.virtualpantry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.expmngr.virtualpantry.Database.FoodDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {
    public static FoodDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room.databaseBuilder(getApplicationContext(),FoodDatabase.class,"fooddb").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        Button inputMenu = (Button) findViewById(R.id.inputMenu);
        inputMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InputMenu.class);
                startActivity(intent);
            }
        });

        Button readCSV = (Button) findViewById(R.id.csvTestButton);
        readCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), readCSVFiles.class));
            }
        });


    }


}
