package com.expmngr.virtualpantry.Database;

import com.expmngr.virtualpantry.Database.DataAccessObjects.ExpiredFoodDAO;
import com.expmngr.virtualpantry.Database.DataAccessObjects.ExpiryFoodDAO;
import com.expmngr.virtualpantry.Database.Entities.ExpiredFood;
import com.expmngr.virtualpantry.Database.Entities.ExpiryFood;
import com.expmngr.virtualpantry.Database.Entities.Food;
import com.expmngr.virtualpantry.Database.DataAccessObjects.FoodDAO;
import com.expmngr.virtualpantry.Database.Entities.FoodGroup;
import com.expmngr.virtualpantry.Database.DataAccessObjects.FoodGroupDAO;
import com.expmngr.virtualpantry.Database.Entities.FoodTemplates;
import com.expmngr.virtualpantry.Database.DataAccessObjects.FoodTemplatesDAO;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Food.class, ExpiryFood.class, FoodTemplates.class, FoodGroup.class, ExpiredFood.class}, version = 9)
public abstract class FoodDatabase extends RoomDatabase {
    public abstract FoodDAO foodDAO();
    public abstract ExpiryFoodDAO expiryFoodDAO();
    public abstract FoodTemplatesDAO foodTemplatesDAO();
    public abstract FoodGroupDAO foodGroupDAO();
    public abstract ExpiredFoodDAO expiredFoodDAO();

}

