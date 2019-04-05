package com.expmngr.virtualpantry;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FoodGroupDAO {

    @Insert
     void addFoodGroup(FoodGroup group);

    @Query("select * from FoodGroup")
    List<FoodGroup> getFoodGroup();

    @Delete
     void deleteFoodGroup(FoodGroup group);

}
