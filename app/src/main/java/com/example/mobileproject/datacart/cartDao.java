package com.example.mobileproject.datacart;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.DeleteColumn;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface cartDao {
    @Query("SELECT * FROM cart")
    LiveData<List<Cart>> getallcartitems();
    @Insert
    void insertAll( Cart ... cart);
    @Delete
    void deletefrom( Cart cart);


}
