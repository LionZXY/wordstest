package com.words.dzolla.wordstest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by eee on 16.11.2016.
 */
public class SelectOrCreateCourseActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "MyApp";
    Button createCourseButton;
    LinearLayout root_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_or_create_course_layout);
        createCourseButton = (Button) findViewById(R.id.create_course_button);
        createCourseButton.setOnClickListener(this);

        showButtons();

    }

    @Override
    public void onClick(View v) {
        if (!(v.getId() == R.id.create_course_button)) {

            Intent intent = new Intent(this, CampaignOrSurvivalActivity.class);
            int courseId = (int) v.getTag();
            intent.putExtra("courseId", courseId);
            DBHelper mDBHelper = new DBHelper(this);
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor c = db.query("courses", null, "courses._id="+courseId, null, null,null,null);
            c.moveToFirst();
            intent.putExtra("origLangId", c.getInt(1));
            intent.putExtra("targetLangId", c.getInt(2));
            startActivity(intent);

        } else {
            DBHelper mDBHelper = new DBHelper(this);
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("_id", 3);
            values.put("orig_lang_id", 3);
            values.put("target_lang_id", 2);
            try {
                db.insertOrThrow("courses", null, values);
            } catch (SQLException e) {
                Log.e("Exception", "SQLException" + String.valueOf(e.getMessage()));
                e.printStackTrace();
            }
            showButtons();
        }

    }

    void showButtons() {
        root_layout = (LinearLayout) findViewById(R.id.root_layout);
        root_layout.removeAllViews();

        DBHelper mDBHelper = new DBHelper(this);
        mDBHelper.showTableInLog("courses");

        Cursor c = mDBHelper.getTableByName("courses");
        if (c.moveToFirst()) {
            do {
                int courseId = c.getInt(0);
                int origLangId = c.getInt(1);
                int targetLangId = c.getInt(2);
                Button b = new Button(this);
                b.setTag(courseId);
                String textRow =
                        courseId + " "
                                + mDBHelper.getLanguageNameById(origLangId)
                                + " - - - "
                                + mDBHelper.getLanguageNameById(targetLangId);
                b.setText(textRow);
                b.setOnClickListener(this);
                root_layout.addView(b);
            } while (c.moveToNext());

        } else {
            Log.i(TAG, "table is empty");
        }
    }
}
