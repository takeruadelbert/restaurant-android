package com.example.stn_com_01.orderfoodapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stn_com_01.orderfoodapp.API.PostDataOrder;
import com.example.stn_com_01.orderfoodapp.Common.Common;
import com.example.stn_com_01.orderfoodapp.Common.GlobalVariable;
import com.example.stn_com_01.orderfoodapp.Database.Database;
import com.example.stn_com_01.orderfoodapp.Helper.Helper;
import com.example.stn_com_01.orderfoodapp.Model.Order;
import com.example.stn_com_01.orderfoodapp.Model.Request;
import com.example.stn_com_01.orderfoodapp.ViewHolder.CartAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    TextView txtTotalPrice;
    FButton btnSubmit;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    private String total_amount;
    private Map<String,String> dataOrder = new HashMap<String, String>();
    private ArrayList<Map<String, String>> dataOrderDetail = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);

        this.loadListFood();

        btnSubmit = (FButton) findViewById(R.id.btnSubmitOrder);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cart.size() > 0) {
                    showAlertDialog();
                } else {
                    Toast.makeText(Cart.this, "Your cart is emoty..!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        int total = 0;
        for (Order order:cart) {
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
            String resto_menu_id = order.getProductId();
            String quantity = order.getQuantity();
            String amount = order.getPrice();
            String note = order.getNote();
            Map<String, String> order_detail = new HashMap<String, String>();
            order_detail.put("resto_menu_id", resto_menu_id);
            order_detail.put("quantity", quantity);
            order_detail.put("amount", amount);
            order_detail.put("note", note);
            this.dataOrderDetail.add(order_detail);
        }
        this.total_amount = String.valueOf(total);
        Helper helper = new Helper();
        txtTotalPrice.setText(helper.IDR((double)total));
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Order Confirmation");
        alertDialog.setMessage("Enter the table number : ");

        final EditText no_table = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        no_table.setLayoutParams(lp);
        alertDialog.setView(no_table);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Request request = new Request(no_table.getText().toString(), txtTotalPrice.getText().toString(), cart);

                // submit the order request to PHP Server
                final GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
                String print_type = String.valueOf(globalVariable.get_type_print());

                dataOrder.put("no_table", no_table.getText().toString());
                dataOrder.put("total", total_amount);
                dataOrder.put("account_id", Common.username.getAccount_id());
                dataOrder.put("print_type", print_type);

                JSONArray detail_order = new JSONArray(dataOrderDetail);
                System.out.println("Data Order Detail = " + detail_order);
                dataOrder.put("detail", detail_order.toString());
                JSONObject jsonObject = get_request_data();
                PostDataOrder post = new PostDataOrder(Cart.this);

                String ip_address_server = globalVariable.get_ip_address_server();
                String url = "http://" + ip_address_server + "/restaurant/post-data-order";
                System.out.println("sent data = " + jsonObject.toString());
                post.execute(url, jsonObject.toString());

                //Toast.makeText(Cart.this, "Order Submitted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private JSONObject get_request_data() {
        JSONObject jsonObject = new JSONObject(dataOrder);
        return jsonObject;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.DELETE)) {
            deleteCart(item.getOrder());
        }
        return true;
    }

    private void deleteCart(int position) {
        cart.remove(position);
        new Database(this).cleanCart();
        for(Order item: cart) {
            new Database(this).addToCart(item);
        }
        this.loadListFood();
    }
}
