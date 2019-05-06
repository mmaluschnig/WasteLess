package com.expmngr.virtualpantry.AppScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.expmngr.virtualpantry.AppScreens.AddItems.AddItemManually;
import com.expmngr.virtualpantry.AppScreens.AddItems.ScanBarcode;
import com.expmngr.virtualpantry.AppScreens.AddItems.ScanReceipt;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class AddNewItems extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_items);

        setUpButtons();
        setupBottomNavigationView();
    }

    private void setUpButtons(){
        /*
         * Set up OnClickListeners for the manual, receipt, barcode, and import buttons.
         * onClick() will be called when button is pressed.
         */
        Button manualButton = (Button) findViewById(R.id.manualAddButton);
        manualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddItemManually.class));
            }
        });

        Button receiptButton = (Button) findViewById(R.id.scanReceiptButton);
        receiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanReceipt.class));
            }
        });

        Button barcodeButton = (Button) findViewById(R.id.scanBarcodeButton);
        barcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanBarcode.class));
            }
        });



    }

    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigationViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(AddNewItems.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
