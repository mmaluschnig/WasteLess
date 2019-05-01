package com.expmngr.virtualpantry.Database.DataAccessObjects;

import com.expmngr.virtualpantry.Database.Entities.ExpiredFood;
import com.expmngr.virtualpantry.Database.Entities.Food;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ExpiredFoodDAO {

    @Insert
    void addExpiredFood(ExpiredFood expiredfood);

    @Query("select * from expiredfood where isExpired = 1")
    List<ExpiredFood> getExpiredFood();

    @Update
    void updateExpiredFood(ExpiredFood expiredfood);

    @Query("DELETE FROM expiredfood")
    void deleteAllExpiredFood();

    @Delete
    void deleteExpiredFoodFood(ExpiredFood expiredfood);

}
