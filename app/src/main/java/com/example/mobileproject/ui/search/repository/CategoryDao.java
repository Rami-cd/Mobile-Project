package com.example.mobileproject.ui.search.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM category")
    LiveData<List<Category>> getAllCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Category... categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategories(List<Category> categories);

    @Query("SELECT * FROM category WHERE category_name LIKE '%' || :searchQuery || '%'")
    LiveData<List<Category>> searchCategories(String searchQuery);

    @Query("DELETE FROM category")
    void clearCategories();
}
