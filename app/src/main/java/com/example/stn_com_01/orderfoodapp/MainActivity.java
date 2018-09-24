package com.example.stn_com_01.orderfoodapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.stn_com_01.orderfoodapp.Common.GlobalVariable;

public class MainActivity extends AppCompatActivity {

    public Button btnSignIn, btnSetting;
    public TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        txtSlogan = (TextView) findViewById(R.id.txtSlogan);

        // set font to NABILA.TTF
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        txtSlogan.setTypeface(font);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
                if(globalVariable.get_ip_address_server() != null && globalVariable.get_ip_address_server() != "") {
                    Intent signin = new Intent(MainActivity.this, SignIn.class);
                    startActivity(signin);
                } else {
                    Toast.makeText(MainActivity.this, "Error : IP Address Server is not set up yet.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setting = new Intent(MainActivity.this, Setting.class);
                startActivity(setting);
            }
        });
    }
}
