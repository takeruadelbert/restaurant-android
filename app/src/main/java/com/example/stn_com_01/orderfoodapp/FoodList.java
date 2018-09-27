package com.example.stn_com_01.orderfoodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.stn_com_01.orderfoodapp.API.RequestData;
import com.example.stn_com_01.orderfoodapp.API.RequestDataFoodByName;
import com.example.stn_com_01.orderfoodapp.API.RequestDataFoodList;
import com.example.stn_com_01.orderfoodapp.Common.Common;
import com.example.stn_com_01.orderfoodapp.Common.GlobalVariable;
import com.example.stn_com_01.orderfoodapp.Interface.FoodByNameListener;
import com.example.stn_com_01.orderfoodapp.Interface.FoodListener;
import com.example.stn_com_01.orderfoodapp.Model.Food;
import com.example.stn_com_01.orderfoodapp.ViewHolder.FoodListAdapter;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity implements FoodListener, FoodByNameListener{
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    //int menu_category_id;


    // FoodByNameListener Interface
    @Override
    public void onComplete(ArrayList<Food> foods, String text) {
        FoodListAdapter adapter = new FoodListAdapter(this, foods);
        recyclerView.setAdapter(adapter);
    }

    // FoodListener Interface
    @Override
    public void onComplete(ArrayList<Food> foods, List<String> listFood) {
        this.suggestList = listFood;

        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<String>();
                for(String search: suggestList) {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) {
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean b) {
                if(!b) {
//                    recyclerView.setAdapter(recyclerView.getAdapter());
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence charSequence) {
                startSearch(charSequence.toString());
            }

            @Override
            public void onButtonClicked(int i) {

            }
        });

        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        recyclerView = (RecyclerView) findViewById(R.id.recyler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // get Intent from Home
        int menu_category_id = -1;
        if(getIntent() != null) {
            menu_category_id = Integer.parseInt(getIntent().getStringExtra("menu_category_id"));
        }
        if(menu_category_id != -1) {
            if(Common.isConnectedToInternet(this)) {
                recyclerView.setAdapter(new FoodListAdapter(this, loadListFood(menu_category_id)));
            } else {
                Toast.makeText(FoodList.this, "Please check your connection!!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter Menu ...");
    }

    private ArrayList loadListFood(int menu_category_id) {
        RequestDataFoodList req = new RequestDataFoodList(this);
        final GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        String ip_address_server = globalVariable.get_ip_address_server();
        String url = "http://" + ip_address_server + "/restaurant/get-menu-list?category_id=" + menu_category_id;
        req.execute(url, ip_address_server);
        return req.get_foods();
    }

    private ArrayList<Food> startSearch(String text) {
        System.out.println("Char Sequence = " + text);
        RequestDataFoodByName req = new RequestDataFoodByName(this);
        final GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        String ip_address_server = globalVariable.get_ip_address_server();
        String url = "http://" + ip_address_server + "/restaurant/get-menu-by-name?menu_name=" + text;
        req.execute(url, ip_address_server);
        return req.get_foods();
    }
}
