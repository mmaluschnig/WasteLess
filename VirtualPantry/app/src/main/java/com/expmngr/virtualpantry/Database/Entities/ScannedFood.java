package com.expmngr.virtualpantry.Database.Entities;

import java.util.HashMap;
import java.util.Map;

public class ScannedFood extends Food {
    private Map<String,String> expiryDates;

    public ScannedFood() {
        expiryDates = new HashMap<>();
    }

    public Map<String, String> getExpiryDates() {
        return expiryDates;
    }

    public String getDateByLocation(String location){
        return expiryDates.get(location);
    }

    public void setExpiryDates(Map<String, String> expiryDates) {
        this.expiryDates = expiryDates;
    }

    public void addDate(String location, String date){
        this.expiryDates.put(location, date);
    }

    public void setFromFood(Food food){
        this.name = food.getName();
        this.quantity = food.getQuantity();
        this.category = food.getCategory();
        this.location = food.getLocation();
    }
}
