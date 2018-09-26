package com.example.stn_com_01.orderfoodapp.API;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.stn_com_01.orderfoodapp.Database.Database;

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

public class PostDataOrder extends AsyncTask<String, Void, Void> {
    private int http_status;
    private String http_message;
    private Context context;

    public PostDataOrder(Context context) {
        this.context = context;
    }

    public void set_http_status(int code) {
        this.http_status = code;
    }

    public int get_http_status() {
        return this.http_status;
    }

    public void set_http_message(String message) {
        this.http_message = message;
    }

    public String get_http_message() {
        return this.http_message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Sending data ...");
    }

    @Override
    protected Void doInBackground(String... params) {
        String url_api = params[0];
        String order = params[1];
        OutputStream out = null;
        try {
            URL url = new URL(url_api);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(20000); // 20 seconds
            urlConnection.setConnectTimeout(20000); // 20 seconds
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            String sent_data = "order=" + order;

            writer.write(sent_data);
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == urlConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer response = new StringBuffer();
                String line;

                while( (line = br.readLine()) != null) {
                    response.append(line);
                }

                br.close();
                String data_response = response.toString();
                System.out.println("data_response = " + data_response);
                JSONObject obj = new JSONObject(data_response);

                int http_status = Integer.parseInt(obj.get("status").toString());
                this.set_http_status(http_status);
                String http_message = obj.get("message").toString();
                this.set_http_message(http_message);
            } else {
                System.out.println("Error HTTP Status Code : " + responseCode);
                this.set_http_message("Error HTTP Status Code : " + responseCode);
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
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        String message;
        if(this.get_http_status() == 200) {
            message = "Order Submitted!";

            // remove orders from cart when it's successfully submitted to server
            Database db = new Database(this.context);
            db.cleanCart();
        } else {
            message = this.get_http_message();
        }
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
    }
}
