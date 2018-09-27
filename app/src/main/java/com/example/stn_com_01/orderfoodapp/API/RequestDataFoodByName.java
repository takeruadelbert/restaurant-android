package com.example.stn_com_01.orderfoodapp.API;

import android.os.AsyncTask;

import com.example.stn_com_01.orderfoodapp.Interface.FoodByNameListener;
import com.example.stn_com_01.orderfoodapp.Interface.FoodListener;
import com.example.stn_com_01.orderfoodapp.Model.Category;
import com.example.stn_com_01.orderfoodapp.Model.Food;

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
import java.util.List;

public class RequestDataFoodByName extends AsyncTask<String, Void, ArrayList<Food>> {
    private int http_status_code;
    private String http_message;
    private final int read_time_out = 20000; // 20 seconds
    private final int connection_time_out = 20000; // 20 seconds
    private ArrayList<Food> foods;
    private FoodByNameListener foodByNameListener;

    public RequestDataFoodByName(FoodByNameListener foodByNameListener) {
        this.foods = new ArrayList<>();
        this.foodByNameListener = foodByNameListener;
    }

    public int get_http_status_code() {
        return http_status_code;
    }

    public void set_http_status_code(int http_status_code) {
        this.http_status_code = http_status_code;
    }

    public String get_http_message() {
        return http_message;
    }

    public void set_http_message(String http_message) {
        this.http_message = http_message;
    }

    public ArrayList<Food> get_foods() {
        return this.foods;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Requesting Data Food By Name to Server ...");
    }

    @Override
    protected ArrayList<Food> doInBackground(String... params) {
        String urlString = params[0];
        String ip_address_server = params[1];

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-length", "0");
            urlConnection.setUseCaches(true);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setConnectTimeout(this.connection_time_out);
            urlConnection.setReadTimeout(this.read_time_out);
            urlConnection.connect();

            int resposeCode = urlConnection.getResponseCode();
            if (resposeCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                String response_data = sb.toString();
                System.out.println("Response get food by name = " + response_data);
                JSONObject obj = new JSONObject(response_data);
                String temp = obj.get("data").toString();

                int status = Integer.parseInt(obj.get("status").toString());
                this.set_http_status_code(status);

                if (this.get_http_status_code() == 206) {
                    return this.jsonToArray(temp, ip_address_server);
                } else {
                    return this.foods;
                }
            } else {
                System.out.println("Error HTTP Status Code = " + resposeCode);
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
        return this.foods;
    }

    @Override
    protected void onPostExecute(ArrayList<Food> foods) {
        super.onPostExecute(foods);
        if (foodByNameListener != null && this.get_http_status_code() == 206) {
            foodByNameListener.onComplete(foods, foods.get(0).get_name());
        }
    }

    private ArrayList<Food> jsonToArray(String jsonString, String ip_address_server) throws JSONException {
        Food food;
        JSONObject temp = new JSONObject(jsonString);
        String model = temp.get("RestoMenu").toString();
        JSONObject temp2 = new JSONObject(model);

        // get the Resto Menu ID
        int category_id = Integer.parseInt(temp2.get("id").toString());

        // get the Resto Menu Name
        String category_name = temp2.get("name").toString();

            /*
                get the Resto Menu Preview Image URL,
                and append it with Protocol and Server Domain/IP Address
              */
        String url = "http://" + ip_address_server + "/restaurant";
        String image_path = temp2.get("image_path").toString();
        String replaced_image_path = image_path.replaceAll("\\/", "/");
        replaced_image_path = replaced_image_path.replaceAll("\\\\", "/");
        String full_image_path = url + replaced_image_path;

        // get the description
        String description = temp2.get("description").toString();

        // get menu category id
        int menu_category_id = Integer.parseInt(temp2.get("menu_category_id").toString());

        // get the price
        int price = Integer.parseInt(temp2.get("price").toString());

        food = new Food(category_id, category_name, full_image_path, description, menu_category_id, price);
        this.foods.add(food);
        return this.foods;
    }
}