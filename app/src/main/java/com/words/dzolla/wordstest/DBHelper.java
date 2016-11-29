package com.words.dzolla.wordstest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by eee on 11.11.2016.
 */
public class DBHelper extends SQLiteAssetHelper {
    public static final String TAG = "MyApp";

    private static final String DATABASE_NAME = "myDB.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void showTableInLog(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        Log.i(TAG, ">>>TABLE " + tableName);
        Cursor c = db.query(tableName, null, null, null, null, null, null);

        String[] columnNames = c.getColumnNames();
        String columns = "";
        for (String colName : columnNames) {
            columns += "\t" + "\t" + colName;
        }
        Log.i(TAG, columns);

        int columnCount = c.getColumnCount();
//        Log.i(TAG, ""+columnCount);
        if (c.moveToFirst()) {
            do {
                String row = "";
                for (int i = 0; i < columnCount; i++) {
                    row += "\t" + "\t" + c.getString(i);
                }
                Log.i(TAG, row);

            } while (c.moveToNext());
        } else {
            Log.i(TAG, "0 rows");
        }
    }

    public Cursor getTableByName(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        return db.query(tableName, null, null, null, null, null, null);


    }

    public String getLanguageNameById(int languageId) {
        SQLiteDatabase db = getWritableDatabase();
        String[] columns = {"name"};
        Cursor c = db.query("languages", columns, "languages._id=" + languageId,
                null, null, null, null);
        c.moveToFirst();
        String languageName = c.getString(0);
        return languageName;
    }
}
