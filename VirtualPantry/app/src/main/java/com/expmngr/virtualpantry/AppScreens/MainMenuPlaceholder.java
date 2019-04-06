package com.expmngr.virtualpantry.AppScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.expmngr.virtualpantry.R;

public class MainMenuPlaceholder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_placeholder);

        setUpButtons();

    }

    private void setUpButtons(){
        Button addButton = (Button) findViewById(R.id.addItemButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddNewItems.class));
            }
        });

        Button pantryButton = (Button) findViewById(R.id.viewPantryButton);
        pantryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewPantry.class));
            }
        });

        Button settingsButton = (Button) findViewById(R.id.viewSettingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });
    }
}
