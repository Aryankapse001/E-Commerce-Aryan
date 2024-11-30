package com.ecommerce.aryan.Models;

import java.io.Serializable;

public class CartModel implements Serializable {

    ItemModel item;
    Integer count;

    public CartModel(ItemModel item, Integer count) {
        this.item = item;
        this.count = count;
    }

    public ItemModel getItem() {
        return item;
    }

    public void setItem(ItemModel item) {
        this.item = item;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
