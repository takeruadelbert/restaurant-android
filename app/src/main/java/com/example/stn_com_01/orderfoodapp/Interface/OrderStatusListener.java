package com.example.stn_com_01.orderfoodapp.Interface;

import android.util.Log;

import com.example.stn_com_01.orderfoodapp.Model.OrderHistory;

import java.util.ArrayList;

public interface OrderStatusListener {
    void onOrdersCompleted(ArrayList<OrderHistory> orderHistories);
    void onOrdersError(String error);
}
