package com.expmngr.virtualpantry.Database.DataAccessObjects;

import com.expmngr.virtualpantry.Database.Entities.FoodTemplates;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FoodTemplatesDAO {

    @Insert
     void addFoodTemplate(FoodTemplates template);

    @Query("select * from FoodTemplates")
    List<FoodTemplates> getFoodTemplates();

    @Delete
     void deleteFoodTemplate(FoodTemplates template);

}
