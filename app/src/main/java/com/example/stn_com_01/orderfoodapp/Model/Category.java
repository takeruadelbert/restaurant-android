package com.example.stn_com_01.orderfoodapp.Model;

public class Category {
    private int category_id;
    private String name;
    private String image;

    public Category(int category_id, String name, String image) {
        this.setCategory_id(category_id);
        this.setName(name);
        this.setImage(image);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}
