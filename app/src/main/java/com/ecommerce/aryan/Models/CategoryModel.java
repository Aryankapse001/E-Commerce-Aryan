package com.ecommerce.aryan.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CategoryModel {
    @ColumnInfo(name = "name")
    public String name;
    @PrimaryKey(autoGenerate = true)
    public Long id;

    public CategoryModel(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}