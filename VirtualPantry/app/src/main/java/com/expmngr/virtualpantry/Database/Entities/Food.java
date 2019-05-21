package com.expmngr.virtualpantry.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Food {

    @PrimaryKey(autoGenerate = true)
    protected int id;
    protected String name;
    protected float quantity;
    protected String category;
    protected String location;
    protected String date_added;
    protected String expiryDate;
    protected Boolean isExpired;

    public Food() {
    }

    public Food(int id, String name, float quantity, String category, String location, String date_added, String expiryDate, Boolean isExpired) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.category = category;
        this.location = location;
        this.date_added = date_added;
        this.expiryDate = expiryDate;
        this.isExpired = isExpired;
    }

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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getIsExpired(){return isExpired;}

    public void setIsExpired(Boolean expired){this.isExpired = expired;}
}
