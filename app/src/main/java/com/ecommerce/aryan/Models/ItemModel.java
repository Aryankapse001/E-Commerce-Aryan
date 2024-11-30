package com.ecommerce.aryan.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class ItemModel {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "price")
    public Integer price;

    @ColumnInfo(name = "category")
    public String category;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}