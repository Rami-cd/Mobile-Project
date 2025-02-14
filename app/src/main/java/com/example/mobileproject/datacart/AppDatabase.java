package com.example.mobileproject.datacart;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Cart.class},version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract cartDao cartdao();
}
