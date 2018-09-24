package com.example.stn_com_01.orderfoodapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.stn_com_01.orderfoodapp.Common.Common;
import com.example.stn_com_01.orderfoodapp.Model.User;

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

public class PostData extends AsyncTask<String, Void, Void> {
    private int http_status;
    private ProgressDialog dialog;
    private Context context;
    private String http_message;
    private Activity activity;

    public PostData(ProgressDialog mDialog, Context context, Activity activity){
        //set context variables if required
        this.set_http_status(403); // Authorization needed
        this.dialog = mDialog;
        this.context = context;
        this.activity = activity;
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
        System.out.println("Retrieving Data ...");
    }

    @Override
    protected Void doInBackground(String... params) {
        String urlString = params[0]; // URL to call
        String username = params[1]; //data to post
        String password = params[2];
        String data = "username=" + username + "&password=" + password;
        OutputStream out = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(20000); // 20 seconds
            urlConnection.setConnectTimeout(20000); // 20 seconds
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();

            //HAS IT BEEN SUCCESSFUL?
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == urlConnection.HTTP_OK) {
                //GET EXACT RESPONSE
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer response = new StringBuffer();
                String line;

                //READ LINE BY LINE
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                //RELEASE RES
                br.close();
                String data_response = response.toString();
                JSONObject obj = new JSONObject(data_response);

                // get HTTP Status Code from API
                int http_status = Integer.parseInt(obj.get("status").toString());
                String http_message = obj.get("message").toString();
                this.set_http_status(http_status);
                this.set_http_message(http_message);

                // get name of the user from API
                JSONObject temp = obj.getJSONObject("data").getJSONObject("Biodata");
                String name = temp.getString("full_name");

                // get user ID
                JSONObject temp2 = obj.getJSONObject("data").getJSONObject("Account");
                String user_id = temp2.getString("id");

                User user = new User(user_id, name);
                Common.username = user;
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
        if(this.get_http_status() == 206) {
            message = "Login Successful!";
        } else {
            message = this.get_http_message();
        }
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show();
        if(this.get_http_status() == 206) {
            Intent homeIntent = new Intent(this.context, Home.class);
            this.activity.startActivity(homeIntent);
            this.activity.finish();
        }
        this.dialog.dismiss();
    }
}
