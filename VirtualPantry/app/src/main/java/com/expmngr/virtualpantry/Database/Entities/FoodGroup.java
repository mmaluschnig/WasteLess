package com.expmngr.virtualpantry.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FoodGroup {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Integer cat_num;
    private String cat_desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCat_num() {
        return cat_num;
    }

    public void setCat_num(Integer cat_num) {
        this.cat_num = cat_num;
    }

    public String getCat_desc() {
        return cat_desc;
    }

    public void setCat_desc(String cat_desc) {
        this.cat_desc = cat_desc;
    }
}
