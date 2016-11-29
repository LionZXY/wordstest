package com.words.dzolla.wordstest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CampaignOrSurvivalActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MyApp";
    private int courseId, origLangId, targetLangId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_or_survival_layout);
        courseId = getIntent().getIntExtra("courseId", -1);
        origLangId = getIntent().getIntExtra("origLangId", -1);
        targetLangId = getIntent().getIntExtra("targetLangId", -1);

        DBHelper mDBHelper = new DBHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", 29);
        values.put("en_word", "hahahaha");
        values.put("ru_word", "хаhfhfhfhfhха");
        values.put("de_word", "gut");
        values.put("level", 2);
        try {
            db.insertOrThrow("words", null, values);
        } catch (SQLException e) {
            // Sep 12, 2013 6:50:17 AM
            Log.e("Exception", "SQLException" + String.valueOf(e.getMessage()));
            e.printStackTrace();
        }

        mDBHelper.showTableInLog( "words" );
//        DBHelper.showTableInLog("levels", db);
//        DBHelper.showTableInLog("languages", db);
//        DBHelper.showTableInLog("courses", db);

        Button btn1 = (Button) findViewById(R.id.survival);
        btn1.setOnClickListener(this);

        Button btn2 = (Button) findViewById(R.id.campaign);
        btn2.setOnClickListener(this);

        TextView tv = new TextView(this);
        tv.setText(getResources().getString(R.string.courseId) + courseId);
        LinearLayout ll_main = (LinearLayout) findViewById(R.id.ll_main);
        ll_main.addView(tv);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.survival) {
            Intent intent = new Intent(this, SurvivalActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.campaign) {
            Intent intent = new Intent(this, LevelListActivity.class);
            intent.putExtra("courseId", courseId);
            intent.putExtra("origLangId", origLangId);
            intent.putExtra("targetLangId", targetLangId);
            startActivity(intent);
        }
    }


}
