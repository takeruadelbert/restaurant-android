package com.example.stn_com_01.orderfoodapp.Model;

public class User {
    private String account_id;
    private String name;

    public User(String account_id, String name) {
        this.account_id = account_id;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getAccount_id() {
        return this.account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }
}
