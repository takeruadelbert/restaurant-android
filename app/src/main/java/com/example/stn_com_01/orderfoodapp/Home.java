package com.example.stn_com_01.orderfoodapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stn_com_01.orderfoodapp.API.RequestData;
import com.example.stn_com_01.orderfoodapp.Common.Common;
import com.example.stn_com_01.orderfoodapp.Common.GlobalVariable;
import com.example.stn_com_01.orderfoodapp.Interface.CategoryListener;
import com.example.stn_com_01.orderfoodapp.Model.Category;
import com.example.stn_com_01.orderfoodapp.ViewHolder.CategoryAdapter;

import java.util.ArrayList;

import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CategoryListener {

    TextView username;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public void onComplete(ArrayList<Category> categories) {
        recycler_menu.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set name for user
        View headerView = navigationView.getHeaderView(0);
        this.username = (TextView) headerView.findViewById(R.id.username);
        this.username.setText(Common.username.getName());

        // load menu
        recycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        if(Common.isConnectedToInternet(this)) {
            recycler_menu.setAdapter(new CategoryAdapter(this, loadMenu()));
        } else {
            Toast.makeText(Home.this, "Please check your connection!!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.refresh) {
            recycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
            recycler_menu.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recycler_menu.setLayoutManager(layoutManager);
            if(Common.isConnectedToInternet(this)) {
                recycler_menu.setAdapter(new CategoryAdapter(this, loadMenu()));
            } else {
                Toast.makeText(Home.this, "Please check your connection!!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_menu) {
            Intent menu = new Intent(Home.this, Home.class);
            startActivity(menu);
        } else if (id == R.id.nav_cart) {
            Intent cart = new Intent(Home.this, Cart.class);
            startActivity(cart);
        } else if (id == R.id.nav_orders) {
            Intent order = new Intent(Home.this, OrderStatus.class);
            startActivity(order);
        } else if (id == R.id.nav_log_out) {
            // remove username and password which are stored in memory
            Paper.book().destroy();

            Intent back_to_main = new Intent(Home.this, MainActivity.class);
            back_to_main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(back_to_main);
        } else if(id == R.id.nav_setting) {
            Intent setting = new Intent(Home.this, Setting.class);
            startActivity(setting);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ArrayList loadMenu() {
        RequestData get = new RequestData(this);
        final GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        String ip_address_server = globalVariable.get_ip_address_server();
        String url = "http://" + ip_address_server + "/restaurant/get-category";
        get.execute(url, ip_address_server);
        return get.get_categories();
    }
}
