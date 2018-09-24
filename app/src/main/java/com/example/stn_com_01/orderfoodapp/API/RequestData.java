package com.example.stn_com_01.orderfoodapp.API;

import android.content.Context;
import android.os.AsyncTask;

import com.example.stn_com_01.orderfoodapp.Common.GlobalVariable;
import com.example.stn_com_01.orderfoodapp.Interface.CategoryListener;
import com.example.stn_com_01.orderfoodapp.Model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class RequestData extends AsyncTask<String, Void, ArrayList<Category>> {
    private int http_status_code;
    private String http_message;
    private final int read_time_out = 20000; // 20 seconds
    private final int connection_time_out = 20000; // 20 seconds
    private ArrayList<Category> categories;
    private CategoryListener categoryListener;

    public RequestData(CategoryListener categoryListener) {
        this.categories = new ArrayList<>();
        this.categoryListener = categoryListener;
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

    public ArrayList get_categories() {
        return this.categories;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Requesting Data Category to Server ...");
    }

    @Override
    protected ArrayList<Category> doInBackground(String... params) {
        String urlString = params[0];
        String ip_address_server = params[1];

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

            int resposeCode = urlConnection.getResponseCode();
            if(resposeCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                String response_data = sb.toString();
                JSONObject obj = new JSONObject(response_data);
                String temp = obj.get("data").toString();
                return this.jsonToArray(temp, ip_address_server);
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
        return this.categories;
    }

    @Override
    protected void onPostExecute(ArrayList<Category> categories) {
        super.onPostExecute(categories);
        if(categoryListener != null) {
            categoryListener.onComplete(categories);
        }
    }

    private ArrayList<Category> jsonToArray(String jsonString, String ip_address_server) throws JSONException{
        JSONArray jsonArray = new JSONArray(jsonString);
        Category category;
        for(int i = 0; i < jsonArray.length(); i++) {
            String value = jsonArray.getString(i);
            JSONObject temp = new JSONObject(value);
            String model = temp.get("MenuCategory").toString();
            JSONObject temp2 = new JSONObject(model);

            // get the Menu Category ID
            int category_id = Integer.parseInt(temp2.get("id").toString());

            // get the Menu Category Name
            String category_name = temp2.get("name").toString();

            /*
                get the Menu Category Preview Image URL,
                and append it with Protocol and Server Domain/IP Address
              */
            String url = "http://" + ip_address_server + "/restaurant";
            String image_path = temp2.get("image_path").toString();
            String replaced_image_path = image_path.replaceAll("\\/", "/");
            replaced_image_path = replaced_image_path.replaceAll("\\\\", "/");
            String full_image_path = url + replaced_image_path;

            category = new Category(category_id, category_name, full_image_path);
            this.categories.add(category);
        }
        return this.categories;
    }
}
