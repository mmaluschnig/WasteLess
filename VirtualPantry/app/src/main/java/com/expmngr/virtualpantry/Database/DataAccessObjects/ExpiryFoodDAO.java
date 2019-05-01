package com.expmngr.virtualpantry.Database.DataAccessObjects;

import com.expmngr.virtualpantry.Database.Entities.ExpiryFood;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ExpiryFoodDAO {

    @Insert
    void addExpFood(ExpiryFood food);

    @Insert
    void addAllexpFood(ExpiryFood... foods);

    @Query("select * from ExpiryFood")
    List<ExpiryFood> getAllExpFood();

    @Query("DELETE FROM ExpiryFood")
    void deleteAll();
}
