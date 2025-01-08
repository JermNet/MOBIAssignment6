package com.example.m03_bounce;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DBClass extends SQLiteOpenHelper implements DB_Interface {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "Bounce_DB_Name.db";
    private static final String TABLE_NAME = "sample_table";
    private static final String TEXT_TYPE = " TEXT";
    private static final String NUM_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    // Names of columns
    private static final String _ID = "_ID";
    private static final String _COL_1 = "NAME";
    private static final String _COL_2 = "X";
    private static final String _COL_3 = "Y";
    private static final String _COL_4 = "DX";
    private static final String _COL_5 = "DY";
    private static final String _COL_6 = "COLOR";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "NAME VARCHAR(256), X FLOAT, Y FLOAT, DX FLOAT, DY FLOAT, COLOR INTEGER)";
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBClass", "DB onCreate() " + SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
        Log.d("DBClass", "DB onCreate()");

        // Commented out because using raw SQL like this is inconvenient
        /*db.execSQL("INSERT INTO " + TABLE_NAME + " (NAME, X, Y, DX, DY, COLOR)    VALUES" +
                "('Ball', 3.0, 22.1, 0.5, 0.7, -65536)," +
                "('Ball2', 132.0, 22.3, 0.5, -1.7, -65536)," +
                "('Ball3', 134.0, 122.5, 0.5, 2.7, -65536)," +
                "('Ball4', 163.0, 122.7, 0.5, -6.7, -65536)," +
                "('Ball5', 283.0, 222.9, 0.5, 4.7, -65536)");*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        Log.d("DBClass", "DB onUpgrade() to version " + DATABASE_VERSION);
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    /////////// Implement Interface ///////////////////////////
    @Override
    public int count() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        Log.v("DBClass", "getCount=" + cnt);
        return cnt;
    }

    // Save a dataModel to the DB, dump() was used here but has been deleted since that method is not implemented
    @Override
    public int save(DataModel dataModel) {
        Log.v("DBClass", "save()=>  " + dataModel.toString());

        // 1. Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(_COL_1, dataModel.getModelName());
        values.put(_COL_2, dataModel.getModelX());
        values.put(_COL_3, dataModel.getModelY());
        values.put(_COL_4, dataModel.getModelDX());
        values.put(_COL_5, dataModel.getModelDY());
        values.put(_COL_6, dataModel.getModelColor());

        // 3. Insert
        db.insert(TABLE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4.Close
        db.close();
        return 0;
    }

    // Not used but not commented out since it implements from an interface
    @Override
    public int update(DataModel dataModel) {
        return 0;
    }
    @Override
    public int deleteById(Long id) {
        return 0;
    }

    // As mentioned elsewhere in this code, I decided to opt not using any default balls, so this doesn't do anything anymore
    /*private void addDefaultRows() {
        // Call count once
        int doCount = this.count();
        if (doCount > 20) {
            Log.v("DBClass", "already 20 rows in DB");

        } else {

        }

    }*/

    // Find the balls
    @Override
    public List<DataModel> findAll() {
        List<DataModel> temp = new ArrayList<DataModel>();

        // 1. Build the query
        String query = "SELECT  * FROM " + TABLE_NAME;

        // 2. Get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. Go over each row, build and add it to list
        DataModel item;
        if (cursor.moveToFirst()) {
            do {
                item = new DataModel(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2),
                        cursor.getFloat(3), cursor.getFloat(4), cursor.getFloat(5), cursor.getInt(6));
                temp.add(item);
            } while (cursor.moveToNext());
        }

        Log.v("DBClass", "findAll=> " + temp.toString());

        // Return all
        return temp;
    }

    // Intended to be used alongside the clearBalls() method of BouncingBallView so that both the screen and DB are cleared
    public void wipeDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted = db.delete(TABLE_NAME, null, null);
        Log.v("DBClass", "wipeDatabase()=> " + deleted);
        db.close();
    }

    // Never used, but implements from interface, so not commented out
    @Override
    public String getNameById(Long id) {
        return null;
    }

    // Never used, commented out
    /*private void dump() {
    }*/

}
