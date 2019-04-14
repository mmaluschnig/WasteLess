package com.expmngr.virtualpantry.Database;

import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.Database.DataAccessObjects.FoodDAO;
import com.expmngr.virtualpantry.Database.Entities.FoodGroup;
import com.expmngr.virtualpantry.Database.DataAccessObjects.FoodGroupDAO;
import com.expmngr.virtualpantry.Database.Entities.FoodTemplates;
import com.expmngr.virtualpantry.Database.DataAccessObjects.FoodTemplatesDAO;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Food.class, FoodTemplates.class, FoodGroup.class}, version = 2)
public abstract class FoodDatabase extends RoomDatabase {
    public abstract FoodDAO foodDAO();
    public abstract FoodTemplatesDAO foodTemplatesDAO();
    public abstract FoodGroupDAO foodGroupDAO();

}

