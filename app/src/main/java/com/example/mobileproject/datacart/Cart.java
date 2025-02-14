package com.example.mobileproject.datacart;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cart {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="name")
    public String name;
    @ColumnInfo(name="product_id")
    public int product_id;
    @ColumnInfo(name="category_id")
    public int category_id;
    @ColumnInfo(name="shop_id")
    public int shop_id;
    @ColumnInfo(name="price")
    public Double price;
    @ColumnInfo(name="description")
    public String description;
    @ColumnInfo(name="image_url")
    public String image_url;
    @ColumnInfo(name="stock_quantity")
    public int stock_quantity;
}
