package com.example.kaixin.experimentseven;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText newPas = (EditText)findViewById(R.id.newPas);
        final EditText conPas = (EditText)findViewById(R.id.confirm);
        final EditText pas = (EditText)findViewById(R.id.pas);
        final Button btn_ok = (Button)findViewById(R.id.ok);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        final String password = pref.getString("password", "");
        final Boolean success = pref.getBoolean("success", false);
        if (success) {
            pas.setVisibility(View.VISIBLE);
            newPas.setVisibility(View.GONE);
            conPas.setVisibility(View.GONE);
        }
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!success) {
                    if (TextUtils.isEmpty(newPas.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(conPas.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Confirm Password cannot beempty!", Toast.LENGTH_SHORT).show();
                    } else if (!newPas.getText().toString().equals(conPas.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Password Mismatch!", Toast.LENGTH_SHORT).show();
                    } else if (newPas.getText().toString().equals(conPas.getText().toString())){
                        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                        editor.putString("password", conPas.getText().toString());
                        editor.putBoolean("success", true);
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, FileEditorActivity.class);
                        startActivity(intent);
                    }
                } else if (success) {
                    if (TextUtils.isEmpty(pas.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
                    } else if (pas.getText().toString().equals(password)){
                        Intent intent = new Intent(MainActivity.this, FileEditorActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "invalid password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Button btn_clear = (Button)findViewById(R.id.clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPas.setText(null);
                conPas.setText(null);
                pas.setText(null);
            }
        });
    }
}
