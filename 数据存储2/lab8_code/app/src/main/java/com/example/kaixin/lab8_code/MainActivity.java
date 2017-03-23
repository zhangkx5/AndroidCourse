package com.example.kaixin.lab8_code;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private myDB dbHelper;
    private ListView lv;
    private ArrayList<HashMap<String, String>> listItems;
    private SimpleAdapter simpleAdapter;
    private TextView et_number, et_name;
    private EditText et_birthday, et_gift;
    private Button btn_nosave, btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new myDB(MainActivity.this, "birthday.db", null, 2);
        lv = (ListView)findViewById(R.id.list_item);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("是否删除？");
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("myTable", "name = ?", new String[] {listItems.get(position).get("name")});
                        db.close();
                        listItems.remove(position);
                        simpleAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View vi = factory.inflate(R.layout.dialoglayout, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(vi);
                final Dialog dialog = builder.create();
                dialog.show();
                et_name = (TextView)vi.findViewById(R.id.et_name);
                et_name.setText(listItems.get(position).get("name"));
                et_birthday = (EditText)vi.findViewById(R.id.et_birthday);
                et_gift = (EditText)vi.findViewById(R.id.et_gift);
                et_number = (TextView)vi.findViewById(R.id.et_number);
                et_birthday.setHint(listItems.get(position).get("birthday"));
                et_gift.setHint(listItems.get(position).get("gift"));
                btn_nosave = (Button)vi.findViewById(R.id.giveup);
                getPhoneNum();
                btn_nosave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_save = (Button)vi.findViewById(R.id.save);
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDBandList(position);
                        dialog.dismiss();
                    }
                });
            }
        });
        Button btn_add = (Button)findViewById(R.id.add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDB();
    }

    public void showDB() {
        listItems = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("myTable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", cursor.getString(cursor.getColumnIndex("name")));
                map.put("birthday", cursor.getString(cursor.getColumnIndex("birthday")));
                map.put("gift", cursor.getString(cursor.getColumnIndex("gift")));
                listItems.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        simpleAdapter = new SimpleAdapter(this, listItems, R.layout.item,
                new String[]{"name", "birthday", "gift"}, new int[] {R.id.name, R.id.birthday, R.id.gift});
        lv.setAdapter(simpleAdapter);
    }
    public void updateDBandList(int position) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(et_birthday.getText().toString())) {
            values.put("birthday", et_birthday.getText().toString());
            listItems.get(position).put("birthday", et_birthday.getText().toString());
        }
        if (!TextUtils.isEmpty(et_gift.getText().toString())) {
            values.put("gift", et_gift.getText().toString());
            listItems.get(position).put("gift", et_gift.getText().toString());
        }
        db.update("myTable", values, "name = ?", new String[] {listItems.get(position).get("name")});
        db.close();
        simpleAdapter.notifyDataSetChanged();
    }
    public void getPhoneNum() {
        ContentResolver cr = MainActivity.this.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if (et_name.getText().toString().equals(contactName)) {
                Cursor phoneNumbers = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
                if (phoneNumbers.moveToFirst()) {
                    String number = phoneNumbers.getString(phoneNumbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    et_number.setText(number);
                }
                phoneNumbers.close();
                break;
            }
        }
        cursor.close();
    }
}
