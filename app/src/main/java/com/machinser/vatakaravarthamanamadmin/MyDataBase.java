package com.machinser.vatakaravarthamanamadmin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by asnimansari on 21/10/17.
 */

public class MyDataBase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "vatakara.db";
    public static final String TABLE_NAME = "vatakara";
    public static final String COLUMN_ID = "id";
    public static final String MESSAGE = "message";
    public static final String TITLE = "title";



    public MyDataBase(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        Log.e("DB","ON CREATre");
        db.execSQL(
                "create table " + TABLE_NAME+
                        " ("+COLUMN_ID+" integer primary key," +
                        MESSAGE+" text,"+
                        TITLE+" text)"
        );

        Log.e("DB","CREATED TABLE");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertNotificaion (ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();

       long i =  db.insert(TABLE_NAME, null, contentValues);
       db.close();
        Log.e("DB","inserted " + i);
        return true;
    }

    public ArrayList<String> getData() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> s = new ArrayList<>();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME, null );

        res.moveToFirst();
        while (!res.isAfterLast()){
            s.add(res.getString(res.getColumnIndex(MESSAGE)));
            res.moveToNext();
        }
        return s;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }





}