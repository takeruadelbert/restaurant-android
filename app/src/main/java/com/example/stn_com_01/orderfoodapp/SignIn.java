package com.example.stn_com_01.orderfoodapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.stn_com_01.orderfoodapp.Common.GlobalVariable;
import com.rengwuxian.materialedittext.MaterialEditText;

public class  SignIn extends AppCompatActivity {

    public EditText edtUsername, edtPassword;
    public Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtUsername = (MaterialEditText) findViewById(R.id.edtUsername);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Connecting...");
                mDialog.show();

                final GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
                String ip_address_server = globalVariable.get_ip_address_server();

                String url_server = "http://" + ip_address_server + "/restaurant/android-login";
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                PostData post = new PostData(mDialog, SignIn.this, SignIn.this);
                post.execute(url_server, username, password);
            }
        });
    }
}
