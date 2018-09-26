package com.example.stn_com_01.orderfoodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.stn_com_01.orderfoodapp.API.RequestData;
import com.example.stn_com_01.orderfoodapp.API.RequestDataFoodList;
import com.example.stn_com_01.orderfoodapp.Common.Common;
import com.example.stn_com_01.orderfoodapp.Common.GlobalVariable;
import com.example.stn_com_01.orderfoodapp.Interface.FoodListener;
import com.example.stn_com_01.orderfoodapp.Model.Food;
import com.example.stn_com_01.orderfoodapp.ViewHolder.FoodListAdapter;

import java.util.ArrayList;

public class FoodList extends AppCompatActivity implements FoodListener{
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    //int menu_category_id;

    @Override
    public void onComplete(ArrayList<Food> foods) {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        recyclerView = (RecyclerView) findViewById(R.id.recyler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // get Intent from Home
        int menu_category_id = -1;
        if(getIntent() != null) {
            menu_category_id = Integer.parseInt(getIntent().getStringExtra("menu_category_id"));
        }
        if(menu_category_id != -1) {
            if(Common.isConnectedToInternet(this)) {
                recyclerView.setAdapter(new FoodListAdapter(this, loadListFood(menu_category_id)));
            } else {
                Toast.makeText(FoodList.this, "Please check your connection!!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private ArrayList loadListFood(int menu_category_id) {
        RequestDataFoodList req = new RequestDataFoodList(this);
        final GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        String ip_address_server = globalVariable.get_ip_address_server();
        String url = "http://" + ip_address_server + "/restaurant/get-menu-list?category_id=" + menu_category_id;
        req.execute(url, ip_address_server);
        return req.get_foods();
    }
}
