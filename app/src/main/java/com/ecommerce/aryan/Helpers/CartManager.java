package com.ecommerce.aryan.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.databinding.ObservableArrayList;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ecommerce.aryan.Models.CartModel;

import java.lang.reflect.Type;

public class CartManager {
    private static final String CART_PREFS_KEY = "cart_prefs";
    private static final String CART_ITEMS_KEY = "cart_items";

    // Load cart items from SharedPreferences
    public static ObservableArrayList<CartModel> loadCartItems(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CART_PREFS_KEY, Context.MODE_PRIVATE);
        String json = prefs.getString(CART_ITEMS_KEY, null);

        if (json != null) {
            Type type = new TypeToken<ObservableArrayList<CartModel>>(){}.getType();
            return new Gson().fromJson(json, type);
        }

        return new ObservableArrayList<>();
    }

    // Save cart items to SharedPreferences
    public static void saveCartItems(Context context, ObservableArrayList<CartModel> cartItems) {
        SharedPreferences prefs = context.getSharedPreferences(CART_PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String json = new Gson().toJson(cartItems);
        editor.putString(CART_ITEMS_KEY, json);
        editor.apply();
    }

    // Clear cart items from SharedPreferences
    public static void clearCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CART_PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(CART_ITEMS_KEY);
        editor.apply();
    }


}
