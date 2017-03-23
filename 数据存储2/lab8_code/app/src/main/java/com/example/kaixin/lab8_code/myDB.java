package com.example.kaixin.lab8_code;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kaixin on 2016/11/17.
 */

public class myDB extends SQLiteOpenHelper{
    public static final String CREATE_TABLE = "create table myTable"
            +"(name TEXT PRIMARY KEY, birthday TEXT, gift TEXT)";
    private Context mContext;

    public myDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);/*
        sqLiteDatabase.execSQL("insert into myTable(name, birthday, gift)values(?,?,?)",
                new String[] {"zhang", "7.1", "hw"});*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int j) {

    }


}
