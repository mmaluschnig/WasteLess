package com.expmngr.virtualpantry.AppScreens;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.expmngr.virtualpantry.Database.Entities.ExpiryFood;
import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.SettingsVariables;
import com.expmngr.virtualpantry.Utils.SimpleImageArrayAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
//    private static final int LAYOUT_ONE= 0;
//    private static final int LAYOUT_TWO= 1;
    private OnItemClickListener mListener;
    private static HashMap<String,Integer> locationToInt = new HashMap<String,Integer>(){
        {
            put("Pantry", 0);
            put("Fridge", 1);
            put("Freezer", 2);
        }
    };
    private static HashMap<Integer,String> intToLocation = new HashMap<Integer,String>(){
        {
            put(0, "Pantry");
            put(1, "Fridge");
            put(2, "Freezer");
        }
    };

    public interface OnItemClickListener {
        void onSetup(final TextView dateTextView, final Spinner locationSpinner);
        List<ExpiryFood> onItemClick(int position);
        void onDeleteClick(int position);
        void onConfirmEditClick(int position, Food editFood);
        void onLocationChange(int position, String newLocation, TextView dateText);
        void onExpiryFoodChange(int position, int index);
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
        public Spinner locationSpinner;
        public ImageView exp_image;

        private Boolean isEditing = false;
        public String expiryDate;
        public String expiryString;

        public FoodViewHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            deleteImage = itemView.findViewById(R.id.image_delete);
            editImage = itemView.findViewById(R.id.image_edit);
            nameTextView = itemView.findViewById(R.id.food_name);
            catTextView = itemView.findViewById(R.id.food_cat);
            quantityTextView = itemView.findViewById(R.id.food_qty);
            expiryTextView = itemView.findViewById(R.id.food_expiry);
            locationSpinner = itemView.findViewById(R.id.locationSpinner);
            locationSpinner.setEnabled(false);
            locationSpinner.setClickable(false);
            exp_image = itemView.findViewById(R.id.image_exp);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        final int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            List<ExpiryFood> options = listener.onItemClick(position);
                            if(options != null) {
                                PopupMenu popup = new PopupMenu(itemView.getContext(), itemView);
                                final Map<String,Integer> index = new HashMap<>();
                                for (int i=0; i<options.size();i++){
                                    popup.getMenu().add(options.get(i).getName());
                                    index.put(options.get(i).getName(),i);
                                }
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        listener.onExpiryFoodChange(position, index.get(item.getTitle()));

                                        return false;
                                    }
                                });
                                popup.show();
                            }
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

            locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int spinnerPos, long id) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onLocationChange(position, intToLocation.get(spinnerPos), expiryTextView);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

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
                                //create food with edited values
                                Food editFood = new Food();
                                editFood.setName(nameTextView.getText().toString());
                                editFood.setCategory(catTextView.getText().toString());
                                editFood.setQuantity(Float.parseFloat(quantityTextView.getText().toString()));
                                editFood.setLocation(intToLocation.get(locationSpinner.getSelectedItemPosition()));

                                expiryDate = expiryTextView.getText().toString();
                                editFood.setExpiryDate(expiryDate + SettingsVariables.expirytime);

                                listener.onConfirmEditClick(position, editFood);//update food in database

                                //disable edit texts
                                isEditing = false;
                                editImage.setImageResource(R.drawable.ic_edit);
                                nameTextView.setEnabled(false);
                                catTextView.setEnabled(false);
                                quantityTextView.setEnabled(false);
                                locationSpinner.setEnabled(false);
                                locationSpinner.setClickable(false);

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
                                locationSpinner.setEnabled(true);
                                locationSpinner.setClickable(true);
                                expiryTextView.setEnabled(true);
                                expiryTextView.setText(expiryDate);
                            }
                        }
                    }
                }
            });
        }



    }

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


        Spinner locationSpinner = foodViewHolder.locationSpinner;
        final TextView dateTextView = foodViewHolder.expiryTextView;
        mListener.onSetup(dateTextView,locationSpinner);

        //location things
        String location = food.getLocation();
        locationSpinner.setSelection(locationToInt.get(location));

        //Date things
        try {
            String expDate = food.getExpiryDate();
            Date date = new SimpleDateFormat("dd/MM/yyyy HH").parse(expDate);
            foodViewHolder.expiryDate = new SimpleDateFormat("dd/MM/yyyy").format(date);

            foodViewHolder.expiryString = "Expires in: " + getTimeframe(getHoursTillExpiry(foodViewHolder.expiryDate + SettingsVariables.expirytime));
            dateTextView.setText(foodViewHolder.expiryString);

            //set expired image
            if(getTimeframe(getHoursTillExpiry(foodViewHolder.expiryDate + SettingsVariables.expirytime)).equals("Expired")){
                foodViewHolder.exp_image.setImageResource(R.drawable.ic_sick);
            }else{
                foodViewHolder.exp_image.setImageResource(R.drawable.ic_happy);
            }
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
        long difference = (expDate.getTime() - now.getTime());
        difference = difference / 1000 / 60 / 60;
        return (int) difference;
    }


    private static String getTimeframe(int hours){
        if(hours <= 0){
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