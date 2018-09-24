package com.example.stn_com_01.orderfoodapp.Model;

public class OrderHistory {
    private String order_id;
    private String no_order;
    private String order_status_id;
    private String order_date;

    public OrderHistory(String order_id, String no_order, String order_status_id, String order_date) {
        this.setOrder_id(order_id);
        this.setNo_order(no_order);
        this.setOrder_status_id(order_status_id);
        this.setOrder_date(order_date);
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getNo_order() {
        return no_order;
    }

    public void setNo_order(String no_order) {
        this.no_order = no_order;
    }

    public String getOrder_status_id() {
        return order_status_id;
    }

    public void setOrder_status_id(String order_status_id) {
        this.order_status_id = order_status_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }
}
