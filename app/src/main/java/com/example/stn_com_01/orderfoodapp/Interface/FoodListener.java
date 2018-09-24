package com.example.stn_com_01.orderfoodapp.Interface;

import com.example.stn_com_01.orderfoodapp.Model.Food;

import java.util.ArrayList;

public interface FoodListener {
    void onComplete(ArrayList<Food> foods);
}
