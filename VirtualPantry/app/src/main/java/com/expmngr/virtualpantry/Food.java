package com.expmngr.virtualpantry;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Food {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private float quantity;
    private String category;
    private String location;
    private String date_added;
    private float time_till_expiry;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getTime_till_expiry() {
        return time_till_expiry;
    }

    public void setTime_till_expiry(float time_till_expiry) {
        this.time_till_expiry = time_till_expiry;
    }
}
