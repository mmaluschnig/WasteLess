package com.expmngr.virtualpantry.AppScreens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.SettingsVariables;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
//    private static final int LAYOUT_ONE= 0;
//    private static final int LAYOUT_TWO= 1;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onConfirmEditClick(int position, Food editFood);
//        void onAddFoodClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
     }


    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        public ImageView deleteImage;
        public ImageView editImage;
        public TextView nameTextView;
        public TextView catTextView;
        public TextView quantityTextView;
        public TextView expiryTextView;
        public ImageView locationImage;

        private Boolean isEditing = false;
        public String expiryDate;
        public String expiryString;

        public FoodViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            deleteImage = itemView.findViewById(R.id.image_delete);
            editImage = itemView.findViewById(R.id.image_edit);
            nameTextView = itemView.findViewById(R.id.food_name);
            catTextView = itemView.findViewById(R.id.food_cat);
            quantityTextView = itemView.findViewById(R.id.food_qty);
            expiryTextView = itemView.findViewById(R.id.food_expiry);
            locationImage = itemView.findViewById(R.id.image_location);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            //listener.onItemClick(position);
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

            editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            if(isEditing){//finished editing
                                //TODO verify edit input
                                Food editFood = new Food();
                                editFood.setName(nameTextView.getText().toString());
                                editFood.setCategory(catTextView.getText().toString());
                                editFood.setQuantity(Float.parseFloat(quantityTextView.getText().toString()));
                                //editFood.setLocation(nameTextView.getText().toString());

                                expiryDate = expiryTextView.getText().toString();
                                editFood.setExpiryDate(expiryDate);

                                listener.onConfirmEditClick(position, editFood);

                                isEditing = false;
                                editImage.setImageResource(R.drawable.ic_edit);
                                nameTextView.setEnabled(false);
                                catTextView.setEnabled(false);
                                quantityTextView.setEnabled(false);

                                try {
                                    expiryString = "Expires in: " + getTimeframe(getHoursTillExpiry(expiryDate + SettingsVariables.expirytime));
                                    expiryTextView.setText(expiryString);
                                }catch(Exception e){
                                    System.err.println(e);
                                }
                                expiryTextView.setEnabled(false);
                            }else {
                                isEditing = true;
                                editImage.setImageResource(R.drawable.ic_checked);
                                nameTextView.setEnabled(true);
                                catTextView.setEnabled(true);
                                quantityTextView.setEnabled(true);
                                expiryTextView.setEnabled(true);
                                expiryTextView.setText(expiryDate);
                            }
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
        TextView catTextView = foodViewHolder.catTextView;
        catTextView.setText(food.getCategory());
        TextView quantityTextView = foodViewHolder.quantityTextView;
        quantityTextView.setText(new Float(food.getQuantity()).toString());

        ImageView locationImage = foodViewHolder.locationImage;
        String location = food.getLocation();
        if(location.equals("Pantry")){
            locationImage.setImageResource(R.drawable.ic_pantry);
        }else if(location.equals("Fridge")){
            locationImage.setImageResource(R.drawable.ic_food_and_restaurant);
        }else if(location.equals("Freezer")){
            locationImage.setImageResource(R.drawable.ic_snow);
        }
        //TODO Date things
        String expDate = food.getExpiryDate();
        TextView dateTextView = foodViewHolder.expiryTextView;
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy HH").parse(expDate);
            foodViewHolder.expiryDate = new SimpleDateFormat("dd/MM/yyyy").format(date);

            foodViewHolder.expiryString = "Expires in: " + getTimeframe(getHoursTillExpiry(expDate));
            dateTextView.setText(foodViewHolder.expiryString);
        }catch (Exception e){
            System.err.println(e);
        }
    }

    @Override
    public int getItemCount() {
        return mFood.size();
    }

    private static int getHoursTillExpiry(String expiryDate) throws ParseException {
        Date expDate = new SimpleDateFormat("dd/MM/yyyy HH").parse(expiryDate);
        Date now = new Date();
        int difference = (int) (expDate.getTime() - now.getTime());
        difference = difference / 1000 / 60 / 60;

        return difference;
    }

    private static String getTimeframe(int hours){
        if(hours < 0){
            return "Expired";
        }
        if(hours / 24 >= 1){
            int days = hours / 24;
            if(days / 7 >= 1){
                int weeks = days / 7;
                if(days / 30 >= 1){
                    int months = days / 30;
                    if(days / 365 >= 1){
                        int years = days / 365;
                        if(years > 1){
                            return years + " Years";
                        }else{
                            return years + " Year";
                        }
                    }//years
                    if(months > 1){
                        return months + " months";
                    }else{
                        return months + " month";
                    }
                }//months
                if(weeks > 1){
                    return weeks + " weeks";
                }else{
                    return weeks + " week";
                }
            }//weeks
            if(days > 1){
                return days + " days";
            }else{
                return days + " day";
            }
        }else {//days
            if(hours > 1){
                return hours + " hours";
            }else{
                return hours + " hour";
            }
        }//hours
    }
}