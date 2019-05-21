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

    @Query("select * from Food order by date_added ASC")
     List<Food> getFood();

    //Filter by location sort by filter
    @Query("select * from Food where location like :location order by date_added ASC")
    List<Food> getFoodByAdded(String location);

    @Query("select * from Food where location like :location order by expiryDate DESC")
    List<Food> getFoodByExpiry(String location);

    @Query("select * from Food where location=:location order by UPPER(category) ASC")
    List<Food> getFoodByCategory(String location);

    @Query("select * from Food where location=:location order by UPPER(name) ASC")
    List<Food> getFoodByName(String location);

    @Query("select * from Food where location=:location order by quantity ASC")
    List<Food> getFoodByQuantity(String location);

    //get All sort by filter
    @Query("select * from Food order by expiryDate ASC")
    List<Food> getAllByExpiry();

    @Query("select * from Food order by UPPER(category) ASC")
    List<Food> getAllByCategory();

    @Query("select * from Food order by UPPER(name) ASC")
    List<Food> getAllByName();

    @Query("select * from Food order by quantity ASC")
    List<Food> getAllByQuantity();


    //other important functions
    @Query("DELETE FROM Food")
    void deleteAll();

    @Delete
     void deleteFood(Food food);

    @Update
     void updateFood(Food food);

    @Query("select * from Food where isExpired = 1")
    List<Food> getExpiredFood();


    @Deprecated
    @Query("select * from Food where location = 'Pantry'")
    List<Food> getPantryFood();

    @Deprecated
    @Query("select * from Food where location = 'Fridge'")
    List<Food> getFridgeFood();

    @Deprecated
    @Query("select * from Food where location = 'Freezer'")
    List<Food> getFreezerFood();
}
