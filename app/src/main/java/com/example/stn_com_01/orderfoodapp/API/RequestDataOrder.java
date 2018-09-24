package com.example.stn_com_01.orderfoodapp.API;

import android.os.AsyncTask;

import com.example.stn_com_01.orderfoodapp.Interface.OrderStatusListener;
import com.example.stn_com_01.orderfoodapp.Model.OrderHistory;
import com.example.stn_com_01.orderfoodapp.Model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class RequestDataOrder extends AsyncTask<String, Void, ArrayList<OrderHistory>> {
    private int http_status_code;
    private String http_message;
    private final int read_time_out = 20000; // 20 seconds
    private final int connection_time_out = 20000; // 20 seconds
    private ArrayList<OrderHistory> orderHistories;
    private final OrderStatusListener orderStatusListener;

    public RequestDataOrder(OrderStatusListener orderStatusListener) {
        this.orderHistories = new ArrayList<>();
        this.orderStatusListener = orderStatusListener;
    }

    public int getHttp_status_code() {
        return this.http_status_code;
    }

    public String getHttp_message() {
        return this.http_message;
    }

    public ArrayList<OrderHistory> get_order_histories() {
//        for(OrderHistory history:orderHistories) {
//            System.out.println("ORDER ID = " + history.getNo_order());
//        }
        return this.orderHistories;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Requesting Data Order to Server ...");
    }

    @Override
    protected ArrayList<OrderHistory> doInBackground(String... params) {
        String urlString = params[0];

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-length", "0");
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setConnectTimeout(this.connection_time_out);
            urlConnection.setReadTimeout(this.read_time_out);
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                String response_data = sb.toString();
                System.out.println("response data = " + response_data);
                JSONObject obj = new JSONObject(response_data);
                String temp = obj.get("data").toString();
                return this.jsonToArray(temp);
            } else {
                System.out.println("Error HTTP Status Code = " + responseCode);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.orderHistories;
    }

    @Override
    protected void onPostExecute(ArrayList<OrderHistory> orderHistories) {
        super.onPostExecute(orderHistories);
        if(orderStatusListener != null) {
            orderStatusListener.onOrdersCompleted(orderHistories);
        }
    }

    private ArrayList<OrderHistory> jsonToArray(String jsonString) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonString);
        OrderHistory orderHistory;
        for(int i = 0; i < jsonArray.length(); i++) {
            String value = jsonArray.getString(i);
            JSONObject temp = new JSONObject(value);

            // get order ID
            String order_id = temp.get("order_id").toString();

            // get no_order
            String no_order = temp.get("no_order").toString();

            // get order_status_id
            String order_status_id = temp.get("order_status_id").toString();

            // get order date
            String order_date = temp.get("order_date").toString();

            orderHistory = new OrderHistory(order_id, no_order, order_status_id, order_date);
            this.orderHistories.add(orderHistory);
        }
        return this.orderHistories;
    }
}
