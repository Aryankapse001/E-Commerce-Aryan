package com.ecommerce.aryan.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ecommerce.aryan.Models.CategoryModel;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    long insertCategory(CategoryModel category);

    @Query("SELECT * FROM CategoryModel")
    List<CategoryModel> getAllCategories();
}