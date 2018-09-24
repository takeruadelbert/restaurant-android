package com.example.stn_com_01.orderfoodapp.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stn_com_01.orderfoodapp.Interface.ItemClickListener;
import com.example.stn_com_01.orderfoodapp.Model.OrderHistory;
import com.example.stn_com_01.orderfoodapp.R;

import java.util.ArrayList;
import java.util.List;

class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtOrderId, txtOrderStatus, txtOrderDate;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);
        txtOrderId = (TextView) itemView.findViewById(R.id.order_id);
        txtOrderDate = (TextView) itemView.findViewById(R.id.order_date);
        txtOrderStatus = (TextView) itemView.findViewById(R.id.order_status);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    Context c;
    ArrayList<OrderHistory> orderHistories;

    public OrderStatusAdapter(Context c, ArrayList<OrderHistory> orderHistories) {
        this.c = c;
        this.orderHistories = orderHistories;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View v = inflater.inflate(R.layout.order_layout, viewGroup, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder orderViewHolder, int i) {
        final OrderHistory oh = orderHistories.get(i);
        orderViewHolder.txtOrderId.setText(oh.getNo_order());
        orderViewHolder.txtOrderDate.setText(oh.getOrder_date());
        orderViewHolder.txtOrderStatus.setText(this.convert_status_code(oh.getOrder_status_id()));

        orderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(c, oh.getNo_order(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderHistories.size();
    }

    private String convert_status_code(String status) {
        if(status.equals("1")) {
            return "Belum Selesai";
        } else {
            return "Selesai";
        }
    }
}
