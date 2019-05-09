package com.cs441_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class InternalDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ColorEntries.db";
    public static final String TABLE_NAME = "Color_table";

    public static final String COL_0 = "COLOR0";
    public static final String COL_1 = "COLOR1";
    public static final String COL_2 = "COLOR2";
    public static final String COL_3 = "COLOR3";

    public InternalDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate (SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ TABLE_NAME + "(COLOR0 TEXT, COLOR1 TEXT, COLOR2 TEXT, COLOR3 TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertDara(String c0, String c1, String c2, String c3){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_0,c0);
        contentValues.put(COL_1,c1);
        contentValues.put(COL_2,c2);
        contentValues.put(COL_3,c3);

        long result = db.insert(TABLE_NAME, null, contentValues); //will return -1 for false or value of column
        if (result == -1) {
            Log.d("Error adding", "Error while adding");
            return false;
        }
        Log.d("Good adding", "Should be added");

        return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        //* means select all
        Cursor returnMe = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return returnMe;
    }
}
