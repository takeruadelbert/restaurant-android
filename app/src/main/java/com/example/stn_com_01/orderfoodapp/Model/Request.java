package com.example.stn_com_01.orderfoodapp.Model;

import java.util.List;

public class Request {
    private String no_table;
    private String total;
    private String status;
    private List<Order> foods;

    public Request(String no_table, String total, List<Order> foods) {
        this.setNo_table(no_table);
        this.setTotal(total);
        this.setFoods(foods);
        this.status = "1"; // 1 : Belum Selesai, 2 : Selesai
    }

    public String getNo_table() {
        return no_table;
    }

    public void setNo_table(String no_table) {
        this.no_table = no_table;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
