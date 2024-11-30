package com.ecommerce.aryan.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecommerce.aryan.Models.CartModel;
import com.ecommerce.aryan.R;
import com.ecommerce.aryan.databinding.CardCartItemBinding;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    Context context;
    List<CartModel> cartItems;


    CountChangeListener changeListener;
    int selectedPosition;

    public CartAdapter(Context context, List<CartModel> cartItems, CountChangeListener changeListener) {
        this.context = context;
        this.cartItems = cartItems;
        this.changeListener = changeListener;
        selectedPosition = -1;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CartModel cartItem = cartItems.get(position);
        holder.binding.name.setText(cartItem.getItem().getName());
        holder.binding.price.setText("$" + cartItem.getItem().getPrice().toString());
        holder.binding.type.setText(cartItem.getItem().getType());
        holder.binding.count.setText(cartItem.getCount().toString());
        Glide.with(context)
                .load(cartItem.getItem().getImage())
                .centerCrop()
                .placeholder(R.drawable.adidas)
                .into(holder.binding.image);

        holder.binding.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                cartItem.setCount(cartItem.getCount() + 1);
                holder.binding.count.setText(cartItem.getCount().toString());
                changeListener.onIncreased(cartItem, position);
            }
        });
        holder.binding.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                cartItem.setCount(cartItem.getCount() - 1);
                if (cartItem.getCount() != 0) {
                    holder.binding.count.setText(cartItem.getCount().toString());
                }
                changeListener.onDecreased(cartItem, position);

            }
        });


    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CardCartItemBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardCartItemBinding.bind(itemView);
        }
    }

    public interface CountChangeListener {

        void onIncreased(CartModel Item, int position);

        void onDecreased(CartModel Item, int position);

    }
}
