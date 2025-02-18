package com.example.mobileproject.ui.search.repository;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey(autoGenerate = true)
    int category_id;

    @ColumnInfo(name="category_sql_id")
    int sql_id;
    @ColumnInfo(name="category_name")
    String category_name;

    @ColumnInfo(name="category_image_url")
    String category_image_url;

    public Category(String category_name, String category_image_url, int sql_id) {
        this.category_name = category_name;
        this.category_image_url = category_image_url;
        this.sql_id = sql_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_image_url() {
        return category_image_url;
    }

    public int getCategory_id() {
        return sql_id;
    }
}
