package com.ecommerce.aryan.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ecommerce.aryan.Activities.MainActivity;
import com.ecommerce.aryan.Adapters.CartAdapter;
import com.ecommerce.aryan.Models.CartModel;
import com.ecommerce.aryan.R;
import com.ecommerce.aryan.databinding.FragmentCartBinding;


public class CartFragment extends Fragment {
    FragmentCartBinding binding;
    ObservableArrayList<CartModel> cartItems;

    ProgressDialog progressDialog;
    CartAdapter cartAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(getLayoutInflater());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            cartItems = (ObservableArrayList<CartModel>)
                    getArguments().getSerializable("cartItems");
        }
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait.");
        progressDialog.setCancelable(false);
        if (cartItems.size() > 0) {
            binding.totalBox.setVisibility(View.VISIBLE);
        } else {
            binding.totalBox.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Cart is Empty.", Toast.LENGTH_SHORT).show();
        }
        initializeCArt();

        binding.checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageDialog();
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Back Pressed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initializeCArt() {

        cartAdapter = new CartAdapter(getContext(), cartItems, new CartAdapter.CountChangeListener() {
            @Override
            public void onIncreased(CartModel item, int position) {

                setTotal(getTotal());
                binding.headCount.setText(String.valueOf(cartItems.size()));
            }

            @Override
            public void onDecreased(CartModel item, int position) {
                if (item.getCount() < 1) {
                    cartItems.remove(position);
                    cartAdapter.notifyDataSetChanged();
                }binding.headCount.setText(String.valueOf(cartItems.size()));

                setTotal(getTotal());
            }
        });
        binding.headCount.setText(String.valueOf(cartItems.size()));
        binding.rvItems.setAdapter(cartAdapter);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvItems.setHasFixedSize(true);

        if (cartItems.size() > 0) setTotal(getTotal());

        cartAdapter.notifyDataSetChanged();


    }

    public void setTotal(Integer amount) {
        binding.subTotal.setText(amount.toString());
        binding.total.setText(String.valueOf(amount - 10)); // discount of $10
    }

    private Integer getTotal() {
        Integer total = 0;
        for (CartModel c : cartItems) total += (c.getCount() * c.getItem().getPrice());


        return total;
    }

    private void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Your order has been placed successfully.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        ((Activity) getContext()).finish();

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}