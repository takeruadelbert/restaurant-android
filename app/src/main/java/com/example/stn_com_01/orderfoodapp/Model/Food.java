package com.example.stn_com_01.orderfoodapp.Model;

public class Food {
    private int menu_id;
    private String name;
    private String image_path;
    private String description;
    private int menu_category_id;
    private int price;

    public Food(int menu_id, String name, String image_path, String description, int menu_category_id, int price) {
        this.set_menu_id(menu_id);
        this.set_name(name);
        this.set_image_path(image_path);
        this.set_description(description);
        this.set_menu_category_id(menu_category_id);
        this.set_price(price);
    }

    public int get_menu_id() {
        return this.menu_id;
    }

    public void set_menu_id(int menu_id) {
        this.menu_id = menu_id;
    }

    public String get_name() {
        return name;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public String get_image_path() {
        return image_path;
    }

    public void set_image_path(String image_path) {
        this.image_path = image_path;
    }

    public String get_description() {
        return description;
    }

    public void set_description(String description) {
        this.description = description;
    }

    public int get_menu_category_id() {
        return menu_category_id;
    }

    public void set_menu_category_id(int menu_category_id) {
        this.menu_category_id = menu_category_id;
    }

    public int get_price() {
        return price;
    }

    public void set_price(int price) {
        this.price = price;
    }
}
