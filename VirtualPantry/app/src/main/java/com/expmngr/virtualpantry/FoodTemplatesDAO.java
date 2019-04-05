package com.expmngr.virtualpantry;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FoodTemplatesDAO {

    @Insert
     void addFoodTemplate(FoodTemplates template);

    @Query("select * from FoodTemplates")
    List<FoodTemplates> getFoodTemplates();

    @Delete
     void deleteFoodTemplate(FoodTemplates template);

}
