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
     void addFood(Food food);

    @Query("select * from Food order by time_till_expiry ASC")
     List<Food> getFood();

    @Delete
     void deleteFood(Food food);

    @Update
     void updateFood(Food food);

}
