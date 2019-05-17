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
//    private static final int LAYOUT_ONE= 0;
//    private static final int LAYOUT_TWO= 1;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
//        void onAddFoodClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
     }


    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public ImageView deleteImage;

        public FoodViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.food_name);
            deleteImage = itemView.findViewById(R.id.image_delete);

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

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }



    }

//    public static class EmptyFoodViewHolder extends RecyclerView.ViewHolder {
//        public TextView emptyTextView;
//        public ImageView addFoodImage;
//
//        public EmptyFoodViewHolder(View itemView, final OnItemClickListener listener) {
//            super(itemView);
//            emptyTextView = itemView.findViewById(R.id.empty_food);
//            addFoodImage = itemView.findViewById(R.id.add_food);
//
//            addFoodImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null){
//                        int position = getAdapterPosition();
//                        if(position != RecyclerView.NO_POSITION){
//                            listener.onAddFoodClick(position);
//                        }
//                    }
//                }
//            });
//        }
//    }


    private List<Food> mFood;

    public FoodAdapter(List<Food> food) {
        mFood = food;
    }

//    @Override
//    public int getItemViewType(int position)
//    {
//        if(position%2==0)       // Even position
//            return LAYOUT_ONE;
//        else                   // Odd position
//            return LAYOUT_TWO;
//    }

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



//
    }

    @Override
    public int getItemCount() {
        return mFood.size();
    }
}