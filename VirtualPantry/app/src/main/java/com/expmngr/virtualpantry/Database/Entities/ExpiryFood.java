package com.expmngr.virtualpantry.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class ExpiryFood {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String status;
    private String pantryExpiry;
    private String fridgeExpiry;
    private String freezerExpiry;
    private String category;
    private int frequency;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPantryExpiry() {
        return pantryExpiry;
    }

    public void setPantryExpiry(String pantryExpiry) {
        this.pantryExpiry = pantryExpiry;
    }

    public String getFridgeExpiry() {
        return fridgeExpiry;
    }

    public void setFridgeExpiry(String fridgeExpiry) {
        this.fridgeExpiry = fridgeExpiry;
    }

    public String getFreezerExpiry() {
        return freezerExpiry;
    }

    public void setFreezerExpiry(String freezerExpiry) {
        this.freezerExpiry = freezerExpiry;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}