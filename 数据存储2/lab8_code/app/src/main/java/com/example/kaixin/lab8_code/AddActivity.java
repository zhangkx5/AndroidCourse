package com.example.kaixin.lab8_code;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by kaixin on 2016/11/17.
 */

public class AddActivity extends AppCompatActivity {

    private myDB dbHelper;
    private EditText add_name, add_birthday, add_gift;
    private Button btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_info);

        dbHelper = new myDB(AddActivity.this, "birthday.db", null, 2);
        add_name = (EditText)findViewById(R.id.edit_name);
        add_birthday = (EditText)findViewById(R.id.edit_birthday);
        add_gift = (EditText)findViewById(R.id.edit_gift);
        btn_add = (Button)findViewById(R.id.add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(add_name.getText().toString())) {
                    Toast.makeText(AddActivity.this, "名字为空，请完善", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.rawQuery("select * from myTable where name = ?", new String[] {add_name.getText().toString()});
                    if (cursor.moveToFirst()) {
                        Toast.makeText(AddActivity.this, "名字重复，请检查", Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues values = new ContentValues();
                        values.put("name", add_name.getText().toString());
                        values.put("birthday", add_birthday.getText().toString());
                        values.put("gift", add_gift.getText().toString());
                        db.insert("myTable", null, values);/*
                        Intent intent = new Intent(AddActivity.this, MainActivity.class);
                        startActivity(intent);*/
                        AddActivity.this.finish();
                    }
                    cursor.close();
                    db.close();
                }
            }
        });
    }
}