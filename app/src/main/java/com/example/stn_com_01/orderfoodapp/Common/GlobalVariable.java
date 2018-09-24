package com.example.stn_com_01.orderfoodapp.Common;

import android.app.Application;

public class GlobalVariable extends Application{
    private String ip_address_server;
    private int type_print; // 1 : directly sent order to chef, 2 : sent data order to a certain PC to prior review

    public GlobalVariable() {

    }

    public String get_ip_address_server() {
        return this.ip_address_server;
    }

    public void set_ip_address_server(String ip_address_server) {
        this.ip_address_server = ip_address_server;
    }

    public int get_type_print() {
        return this.type_print;
    }

    public void set_type_print(int type_print) {
        this.type_print = type_print;
    }
}
