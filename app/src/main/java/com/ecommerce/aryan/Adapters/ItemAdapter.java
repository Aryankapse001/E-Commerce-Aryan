package com.ecommerce.aryan.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecommerce.aryan.Models.ItemModel;
import com.ecommerce.aryan.R;
import com.ecommerce.aryan.databinding.CardItemBinding;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    
    Context context;
    List<ItemModel> itemList;

    ItemSelectListener itemSelectListener;
    int selectedPosition;


    public ItemAdapter(Context context, List<ItemModel> itemList, ItemSelectListener itemSelectListener) {
        this.context = context;
        this.itemList = itemList;
        this.itemSelectListener = itemSelectListener;
        selectedPosition = -1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_item,parent,false));

    }

    public void setItemList(List<ItemModel> itemList) {
        this.itemList = itemList;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemModel item = itemList.get(position);

        holder.binding.name.setText(item.getName());
        holder.binding.price.setText("$"+item.getPrice().toString());
        holder.binding.type.setText(item.getType());
        Glide.with(context)
                .load(item.getImage())
                .centerCrop()
                .placeholder(R.drawable.adidas)
                .into(holder.binding.image);

        holder.binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                itemSelectListener.onItemSelected(item);
            }
        });



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        CardItemBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardItemBinding.bind(itemView);
        }
    }

    public interface ItemSelectListener {

        void onItemSelected(ItemModel Item);

    }
}
