package com.expmngr.virtualpantry;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Food.class, FoodTemplates.class, FoodGroup.class}, version = 1)
public abstract class FoodDatabase extends RoomDatabase {
    public abstract FoodDAO foodDAO();
    public abstract FoodTemplatesDAO foodTemplatesDAO();
    public abstract FoodGroupDAO foodGroupDAO();

}

