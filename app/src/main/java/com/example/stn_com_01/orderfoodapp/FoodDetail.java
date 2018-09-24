package com.example.stn_com_01.orderfoodapp;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.stn_com_01.orderfoodapp.Database.Database;
import com.example.stn_com_01.orderfoodapp.Helper.Helper;
import com.example.stn_com_01.orderfoodapp.Model.Food;
import com.example.stn_com_01.orderfoodapp.Model.Order;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {
    TextView food_name, food_price, food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    String foodId = "";
    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        food_description = (TextView) findViewById(R.id.food_description);
        food_name = (TextView) findViewById(R.id.food_name);
        food_price = (TextView) findViewById(R.id.food_price);
        food_image = (ImageView) findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        boolean success = false;
        String name = "";
        String image_path = "";
        String description = "";
        double price = 0;
        String menu_id = "";
        String menu_category_id = "";
        if(getIntent() != null) {
            name = getIntent().getStringExtra("name");
            image_path = getIntent().getStringExtra("image_path");
            description = getIntent().getStringExtra("description");
            price = Double.parseDouble(getIntent().getStringExtra("price"));
            menu_id = getIntent().getStringExtra("menu_id");
            menu_category_id = getIntent().getStringExtra("menu_category_id");
            success = true;
        }

        currentFood = new Food( Integer.parseInt(menu_id), name, image_path, description, Integer.parseInt(menu_category_id), (int)price);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        String.valueOf(currentFood.get_menu_id()),
                        currentFood.get_name(),
                        numberButton.getNumber(),
                        String.valueOf(currentFood.get_price())
                ));
//                new Database(getBaseContext()).cleanCart();
                Toast.makeText(FoodDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        if(success) {
            this.set_detail_food(name, image_path, description, price);
        }
    }

    private void set_detail_food(String name, String image_path, String description, double price) {
        collapsingToolbarLayout.setTitle(name);
        food_name.setText(name);
        Helper helper = new Helper();

        // remove html tag in description
        String desc = helper.strip_html_tag(description);
        food_description.setText(desc);

        // add a separator number for price
        String nominal = helper.number_separator(price);
        food_price.setText(nominal);

        Picasso.with(FoodDetail.this).load(image_path).into(food_image);
    }
}
