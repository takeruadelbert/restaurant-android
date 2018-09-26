package com.example.stn_com_01.orderfoodapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.stn_com_01.orderfoodapp.Common.GlobalVariable;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;

public class Setting extends AppCompatActivity {
    private EditText ip_address;
    private RadioGroup print_setting;
    private Button save, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ip_address = (MaterialEditText) findViewById(R.id.ip_address_server);
        print_setting = (RadioGroup) findViewById(R.id.radioPrintSetting);
        save = (Button) findViewById(R.id.btnSave);
        back = (Button) findViewById(R.id.btnBack);

        Paper.init(this);

        // set field 'IP Address Server' input if it has been input by user previously.
        final GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        String ip_address_server = globalVariable.get_ip_address_server();
        if(ip_address_server != "" && ip_address_server != null) {
            ip_address.setText(ip_address_server);
        }

        // set radio button input if it's been input by user prior.
        int print_type_setting_value = globalVariable.get_type_print();
        if(String.valueOf(print_type_setting_value) != "" && String.valueOf(print_type_setting_value) != null) {
            switch (print_type_setting_value) {
                case 1 :
                    print_setting.check(R.id.radioDirectlyPrint);
                    break;
                case 2 :
                    print_setting.check(R.id.radioUndirectlyPrint);
                    break;
            }
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip_address_server = ip_address.getText().toString();
                int print_type_value = 1; // by default
                int radioButtonId = print_setting.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.radioDirectlyPrint :
                        print_type_value = 1;
                        break;
                    case R.id.radioUndirectlyPrint :
                        print_type_value = 2;
                        break;
                }
                if(validate_ip_address_server_input(ip_address_server)) {
                    ProgressDialog mDialog = new ProgressDialog(Setting.this);
                    mDialog.setMessage("Saving current setting...");
                    mDialog.show();
                    if(set_value_setting(ip_address_server, print_type_value)) {
                        Toast.makeText(Setting.this, "Successfully Set.", Toast.LENGTH_LONG).show();
//                        Intent main = new Intent(Setting.this, MainActivity.class);
//                        startActivity(main);
                        finish();
                    } else {
                        Toast.makeText(Setting.this, "There's something wrong with the input.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Setting.this, "Invalid IP Address Input!", Toast.LENGTH_LONG).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent back_to_main = new Intent(Setting.this, MainActivity.class);
//                startActivity(back_to_main);
                finish();
            }
        });
    }

    private boolean set_value_setting(String ip_address, int print_type) {
        try {
            // calling Application Class in AndroidManifest.xml
            final GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();

            // set IP Address Server from input
            globalVariable.set_ip_address_server(ip_address);

            // set Print Type after submit order
            globalVariable.set_type_print(print_type);

            // write settings to memory
            Paper.book().write("ip_address_server", ip_address);
            Paper.book().write("print_type", String.valueOf(print_type));

            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean validate_ip_address_server_input(String ip_address) {
        return Patterns.IP_ADDRESS.matcher(ip_address).matches();
    }
}
