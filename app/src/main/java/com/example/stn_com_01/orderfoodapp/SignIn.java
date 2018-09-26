package com.example.stn_com_01.orderfoodapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stn_com_01.orderfoodapp.Common.Common;
import com.example.stn_com_01.orderfoodapp.Common.GlobalVariable;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class  SignIn extends AppCompatActivity {

    public EditText edtUsername, edtPassword;
    public Button btnSignIn;
    CheckBox checkboxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Paper.init(this);

        // check if username and password is already stored. if so, auto login.
        String username = Paper.book().read(Common.USERNAME);
        String password = Paper.book().read(Common.PASSWORD);
        String ip_address_server = Paper.book().read("ip_address_server");
        String print_type = Paper.book().read("print_type");
        if( (username != null && !username.isEmpty()) && (password != null && !password.isEmpty()) && (ip_address_server != null && !ip_address_server.isEmpty()) && (print_type != null && !print_type.isEmpty())) {
            // set global variable of IP Address Server and Print Type Setting
            final GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
            globalVariable.set_ip_address_server(ip_address_server);
            globalVariable.set_type_print(Integer.parseInt(print_type));

            login(username, password);
        }

        edtUsername = (MaterialEditText) findViewById(R.id.edtUsername);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        checkboxRememberMe = (CheckBox) findViewById(R.id.checkboxRememberMe);

        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(Common.isConnectedToInternet(getBaseContext())) {

                    // store username and password to memory phone if checkbox is checked
                    if(checkboxRememberMe.isChecked()) {
                        Paper.book().write(Common.USERNAME, edtUsername.getText().toString());
                        Paper.book().write(Common.PASSWORD, edtPassword.getText().toString());
                    }
                    String username = edtUsername.getText().toString();
                    String password = edtPassword.getText().toString();
                    login(username, password);
                } else {
                    Toast.makeText(SignIn.this, "Please check your connection!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void login(String username, String password) {
        ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Connecting...");
        mDialog.show();

        final GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        String ip_address_server = globalVariable.get_ip_address_server();

        String url_server = "http://" + ip_address_server + "/restaurant/android-login";

        PostData post = new PostData(mDialog, SignIn.this, SignIn.this);
        post.execute(url_server, username, password);
    }
}
