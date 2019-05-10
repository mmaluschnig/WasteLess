package com.expmngr.virtualpantry.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ExpiredFood {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private float quantity;
    private String category;
    private String location;
    private String date_added;
    private String expiryDate;
    private Boolean isExpired;


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

    @Override
    public String toString() {
        return "ExpiredFood{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", date_added='" + date_added + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", isExpired=" + isExpired +
                '}';
    }
}
