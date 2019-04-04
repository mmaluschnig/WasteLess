package com.expmngr.virtualpantry;

import androidx.room.RoomDatabase;

public abstract class FoodDatabase extends RoomDatabase {
    public abstract FoodDAO foodDAO();
}

