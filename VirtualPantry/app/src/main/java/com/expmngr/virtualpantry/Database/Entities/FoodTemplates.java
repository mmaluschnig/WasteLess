package com.expmngr.virtualpantry.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FoodTemplates {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String longName;
    private String shortName;
    private Integer category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}
