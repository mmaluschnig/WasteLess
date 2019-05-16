package com.expmngr.virtualpantry.AppScreens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

     public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
     }


    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public Button removeButton;

        public FoodViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);

            nameTextView = itemView.findViewById(R.id.food_name);
            removeButton = itemView.findViewById(R.id.remove_button);

            itemView .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
                    //deleteImage = itemView.findViewById(R.id.image_delete);

                    //deleteImage.setOnClickListener(new View.OnClickListener(){

//                @Override
//                public void onClick(View v) {
//                    if (listener != null){
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION){
//                            listener.onDeleteClick(position);
//                        }
//                    }
//                }
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

        FoodViewHolder foodViewHolder = new FoodViewHolder(foodView, mListener);
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