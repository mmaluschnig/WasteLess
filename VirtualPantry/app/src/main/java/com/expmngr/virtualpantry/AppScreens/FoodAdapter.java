package com.expmngr.virtualpantry.AppScreens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {


    public class FoodViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public Button removeButton;

        public FoodViewHolder(View itemView) {

            super(itemView);

            nameTextView = itemView.findViewById(R.id.food_name);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }

    private List<Food> mFood;

    public FoodAdapter(List<Food> food) {
        mFood = food;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View foodView = inflater.inflate(R.layout.item_food, parent, false);

        FoodViewHolder foodViewHolder = new FoodViewHolder(foodView);
        return foodViewHolder;
    }

    @Override
    public void onBindViewHolder(FoodViewHolder foodViewHolder, int position) {

        Food food = mFood.get(position);


        TextView nameTextView = foodViewHolder.nameTextView;
        nameTextView.setText(food.getName());



        Button button = foodViewHolder.removeButton;
        button.setText("Remove");
    }

    @Override
    public int getItemCount() {
        return mFood.size();
    }
}