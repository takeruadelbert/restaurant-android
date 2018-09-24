package com.example.stn_com_01.orderfoodapp.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.stn_com_01.orderfoodapp.FoodDetail;
import com.example.stn_com_01.orderfoodapp.FoodList;
import com.example.stn_com_01.orderfoodapp.Model.Category;
import com.example.stn_com_01.orderfoodapp.Model.Food;
import com.example.stn_com_01.orderfoodapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodViewHolder>{
    Context c;
    ArrayList<Food> foods;

    public FoodListAdapter(Context c, ArrayList<Food> foods) {
        this.c = c;
        this.foods = foods;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(c).inflate(R.layout.food_item, viewGroup, false);
        return new FoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        // current category
        final Food f = foods.get(position);

        // bind the data
        holder.food_name.setText(f.get_name());
        Picasso.with(this.c).load(f.get_image_path()).into(holder.food_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(c, f.get_name(), Toast.LENGTH_SHORT).show();
                Intent foodDetail = new Intent(c, FoodDetail.class);
                foodDetail.putExtra("menu_id", String.valueOf(f.get_menu_id()));
                foodDetail.putExtra("name", f.get_name());
                foodDetail.putExtra("image_path", f.get_image_path());
                foodDetail.putExtra("description", f.get_description());
                foodDetail.putExtra("price", f.get_price() + "");
                foodDetail.putExtra("menu_category_id", String.valueOf(f.get_menu_category_id()));
                c.startActivity(foodDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
}
