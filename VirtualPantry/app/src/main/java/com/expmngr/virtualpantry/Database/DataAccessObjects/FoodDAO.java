package com.expmngr.virtualpantry.Database.DataAccessObjects;

import com.expmngr.virtualpantry.Database.Entities.Food;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FoodDAO {

    @Insert
     void addFood(Food food);

    @Query("select * from Food order by expiryDate ASC")
     List<Food> getFood();

    @Query("DELETE FROM Food")
    void deleteAll();

    @Delete
     void deleteFood(Food food);

    @Update
     void updateFood(Food food);

    @Query("select * from Food where isExpired = 1")
    List<Food> getExpiredFood();
}
