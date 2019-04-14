package com.expmngr.virtualpantry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.expmngr.virtualpantry.Database.Entities.Food;

import java.util.List;

public class test1 extends AppCompatActivity {
    TextView txtInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);


        txtInfo = (TextView) findViewById(R.id.viewFood);

        List<Food> food = MainActivity.database.foodDAO().getFood();

        String info = "";

        for(Food f : food){
            int id = f.getId();
            String name = f.getName();
            float quantity = f.getQuantity();
            //Integer time2exp = f.getTime_till_expiry();

            info = info + "\n ID : " + id + "\n Name : " + name + "\n Quantity : " + quantity + "\n Expires in: " + "placeholder Date" + " hours\n";
        }
        txtInfo.setText(info);
    }
}
