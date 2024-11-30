package com.ecommerce.aryan.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ecommerce.aryan.Models.CategoryModel;
import com.ecommerce.aryan.Models.ItemModel;

@Database(entities = {CategoryModel.class, ItemModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
    public abstract ItemDao itemDao();

    private static AppDatabase instance;
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "product_db").allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}