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

    @Query("select * from Food where location = 'Pantry'")
     List<Food> getPantryFood();

    @Query("select * from Food where location = 'Fridge'")
    List<Food> getFridgeFood();

    @Query("select * from Food where location = 'Freezer'")
    List<Food> getFreezerFood();

    @Query("select * from Food where location like :location order by expiryDate ASC")
    List<Food> getFoodByExpiry(String location);

    @Query("select * from Food where location=:location order by category ASC")
    List<Food> getFoodByCategory(String location);

    @Query("select * from Food where location=:location order by name ASC")
    List<Food> getFoodByName(String location);

    @Query("select * from Food where location=:location order by quantity ASC")
    List<Food> getFoodByQuantity(String location);

    @Query("DELETE FROM Food")
    void deleteAll();

    @Delete
     void deleteFood(Food food);

    @Update
     void updateFood(Food food);

    @Query("select * from Food where isExpired = 1")
    List<Food> getExpiredFood();
}
