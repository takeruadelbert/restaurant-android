package com.example.stn_com_01.orderfoodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.stn_com_01.orderfoodapp.API.RequestDataOrder;
import com.example.stn_com_01.orderfoodapp.Common.Common;
import com.example.stn_com_01.orderfoodapp.Common.GlobalVariable;
import com.example.stn_com_01.orderfoodapp.Interface.OrderStatusListener;
import com.example.stn_com_01.orderfoodapp.Model.OrderHistory;
import com.example.stn_com_01.orderfoodapp.ViewHolder.OrderStatusAdapter;

import java.util.ArrayList;

public class OrderStatus extends AppCompatActivity  implements OrderStatusListener {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    public void onOrdersCompleted(ArrayList<OrderHistory> orderHistories) {
//        Log.d("Size = ", "" + orderHistories.size());
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void onOrdersError(String error) {
        Log.d("Error", "error");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        recyclerView = (RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new OrderStatusAdapter(this, load_order_history()));
    }

    private ArrayList<OrderHistory> load_order_history() {
        RequestDataOrder req = new RequestDataOrder(this);
        final GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        String ip_address_server = globalVariable.get_ip_address_server();
        String account_id = Common.username.getAccount_id();
        String url = "http://" + ip_address_server + "/restaurant/get-data-order-status?account_id=" + account_id;
        req.execute(url);
        System.out.println("aaa");
        System.out.println("get_order_histories = " + req.get_order_histories());
        return req.get_order_histories();
    }
}
