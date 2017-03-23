package com.example.kaixin.experimentfour;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by kaixin on 2016/10/21.
 */
public class DynamicActivity extends Activity {

    private DynamicReceiver dynamicReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_layout);

        Button btn_send = (Button) findViewById (R.id.btn_send);
        final EditText input = (EditText)findViewById(R.id.input);
        final Button btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_register.getText().toString().equals("Register Broadcast")) {
                    dynamicReceiver = new DynamicReceiver();
                    IntentFilter dynamic_filter = new IntentFilter();
                    dynamic_filter.addAction("com.example.kaixin.experimentfour.dynamicreceiver");
                    registerReceiver(dynamicReceiver, dynamic_filter);
                    btn_register.setText("UnRegister Broadcast");
                } else if (btn_register.getText().toString().equals("UnRegister Broadcast")) {
                    unregisterReceiver(dynamicReceiver);
                    btn_register.setText("Register Broadcast");
                }
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(input.getText())) {
                    Intent intent = new Intent("com.example.kaixin.experimentfour.dynamicreceiver");
                    intent.putExtra("name", input.getText().toString());
                    sendBroadcast(intent);
                }
            }
        });
    }
}
