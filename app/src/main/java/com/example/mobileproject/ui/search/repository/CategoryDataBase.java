package com.example.mobileproject.ui.search.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities= {Category.class}, version = 1)
public abstract class CategoryDataBase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
}
