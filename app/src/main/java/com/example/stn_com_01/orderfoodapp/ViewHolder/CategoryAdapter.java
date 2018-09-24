package com.example.stn_com_01.orderfoodapp.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.stn_com_01.orderfoodapp.FoodList;
import com.example.stn_com_01.orderfoodapp.Home;
import com.example.stn_com_01.orderfoodapp.Model.Category;
import com.example.stn_com_01.orderfoodapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<MenuViewHolder>{
    Context c;
    ArrayList<Category> categories;

    public CategoryAdapter(Context c, ArrayList<Category> categories) {
        this.c = c;
        this.categories = categories;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(c).inflate(R.layout.menu_item, viewGroup, false);
        return new MenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        // current category
        final Category cat = categories.get(position);

        // bind the data
        holder.txtMenuName.setText(cat.getName());
        Picasso.with(this.c).load(cat.getImage()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(c, "Category ID = " + cat.getCategory_id() + ", Category Name = " + cat.getName(), Toast.LENGTH_SHORT).show();
                Intent foodList = new Intent(c, FoodList.class);
                foodList.putExtra("menu_category_id", cat.getCategory_id() + "");
                c.startActivity(foodList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
