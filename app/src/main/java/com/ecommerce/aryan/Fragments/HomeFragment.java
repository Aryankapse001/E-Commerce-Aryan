package com.ecommerce.aryan.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ecommerce.aryan.Adapters.BannerPagerAdapter;
import com.ecommerce.aryan.Adapters.CategoriesAdapter;
import com.ecommerce.aryan.Adapters.ItemAdapter;
import com.ecommerce.aryan.Database.AppDatabase;
import com.ecommerce.aryan.Models.CartModel;
import com.ecommerce.aryan.Models.CategoryModel;
import com.ecommerce.aryan.Models.ItemModel;
import com.ecommerce.aryan.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    BannerPagerAdapter bannerAdapter;

    CategoriesAdapter categoriesAdapter;

    List<CategoryModel> categories;
    List<ItemModel> items;

    ObservableArrayList<CartModel> cartItems;
    ItemAdapter itemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAutoScrollBanner();

        if (getArguments() != null) {
            cartItems = (ObservableArrayList<CartModel>)
                    getArguments().getSerializable("cartItems");
        }


    }

    private void setCatLog() {
        AppDatabase db = AppDatabase.getInstance(getContext());
        categories = db.categoryDao().getAllCategories();

        categoriesAdapter = new CategoriesAdapter(getContext(), categories, new CategoriesAdapter.CategorySelectListener() {
            @Override
            public void onCategorySelected(CategoryModel category) {
                items.clear();
                items.addAll(db.itemDao().getItemsForCategory(category.getName()));
                //Toast.makeText(getContext(), String.valueOf(items.size()), Toast.LENGTH_SHORT).show();
                itemAdapter.notifyDataSetChanged();
                categoriesAdapter.notifyDataSetChanged();
            }
        });

        binding.rvCategory.setAdapter(categoriesAdapter);
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategory.setHasFixedSize(true);

        items = new ArrayList<>();
        itemAdapter = new ItemAdapter(getContext(), items, new ItemAdapter.ItemSelectListener() {
            @Override
            public void onItemSelected(ItemModel Item) {
                if (cartItems != null) {
                    cartItems.add(new CartModel(Item, 1));
                    Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.rvItems.setAdapter(itemAdapter);
        binding.rvItems.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvItems.setHasFixedSize(true);

        categoriesAdapter.getCategorySelectListener().onCategorySelected(categories.get(0));
        categoriesAdapter.setSelectedPosition(0);


    }

    private void setAutoScrollBanner() {
        bannerAdapter = new BannerPagerAdapter(getContext());
        binding.bannerKocMarket.setAdapter(bannerAdapter);
        binding.tabDots.setupWithViewPager(binding.bannerKocMarket, true);

        binding.bannerKocMarket.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // Handle page selection
            }
        });


        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = binding.bannerKocMarket.getCurrentItem();
                int nextItem = (currentItem + 1) % bannerAdapter.getCount();
                binding.bannerKocMarket.setCurrentItem(nextItem, true);
                handler.postDelayed(this, 5000);
            }
        };


        handler.postDelayed(runnable, 5000);
    }

    @Override
    public void onStart() {
        super.onStart();
        new bgThread().start();
    }

    class bgThread extends Thread {
        @Override
        public void run() {
            super.run();


            setCatLog();
        }
    }
}