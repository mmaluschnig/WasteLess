package com.expmngr.virtualpantry.AppScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.expmngr.virtualpantry.AppScreens.AddItems.FeedbackScreen;
import com.expmngr.virtualpantry.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button feedbackBtn = (Button) findViewById(R.id.settingsOpenFeedbackPage);
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FeedbackScreen.class);
                startActivity(intent);
            }
        });

    }
}
