package com.words.dzolla.wordstest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class LevelListActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MyApp";
    int courseId, origLangId, targetLangId;
    LinearLayout ll_levels;

    DBHelper mDBHelper;
    SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_list_layout);
        Log.i(TAG, "Activity launched");

//        courseId = getIntent().getIntExtra("courseId", -1);
//        origLangId = getIntent().getIntExtra("origLangId", -1);
//        targetLangId = getIntent().getIntExtra("targetLangId", -1);

        courseId = 1;
        origLangId = 1;
        targetLangId = 2;

        mDBHelper = new DBHelper(this);
        mDB = mDBHelper.getWritableDatabase();

        showLevels();

    }

    @Override
    public void onClick(View v) {

        int levelId = (int) v.getTag();
        Intent intent = new Intent(this, LevelActivity.class);
        intent.putExtra("levelId", levelId);
        intent.putExtra("targetLangId", targetLangId);
        intent.putExtra("origLangId", origLangId);
        startActivity(intent);
    }


    private void showLevels() {
        ll_levels = (LinearLayout) findViewById(R.id.ll_levels);
//        mDBHelper = new DBHelper(this);
//        mDB = mDBHelper.getWritableDatabase();

        mDBHelper.showTableInLog("levels");

        //получаем уровни
        String[] lvlColumns = new String[2];
        lvlColumns[0] = "_id";

        if (origLangId == 1) {
            lvlColumns[1] = "en_name";
        } else if (origLangId == 2) {
            lvlColumns[1] = "ru_name";
        } else if (origLangId == 3) {
            lvlColumns[1] = "de_name";
        }

        Cursor curLevels = mDB.query("levels", lvlColumns, null, null, null, null, null);
        if (curLevels.moveToFirst()) {
            do {
//
                int levelId = curLevels.getInt(0);
                String levelName = curLevels.getString(1);
                Button b = new Button(this);
                b.setTag(levelId);
                String row = levelId + " " + levelName;
                b.setText(row);
                b.setOnClickListener(this);
                ll_levels.addView(b);


            } while (curLevels.moveToNext());

        } else {
            Log.i(TAG, "table is empty");
        }
    }


}
