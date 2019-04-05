package com.expmngr.virtualpantry.Database.DataAccessObjects;

import com.expmngr.virtualpantry.Database.Entities.FoodGroup;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FoodGroupDAO {

    @Insert
     void addFoodGroup(FoodGroup group);

    @Query("select * from FoodGroup")
    List<FoodGroup> getFoodGroup();

    @Delete
     void deleteFoodGroup(FoodGroup group);

}
