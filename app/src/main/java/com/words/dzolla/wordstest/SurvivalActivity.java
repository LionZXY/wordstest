package com.words.dzolla.wordstest;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class SurvivalActivity extends AppCompatActivity implements View.OnClickListener {

    public static final java.lang.String TAG = "MyApp";
    Dictionary dict, dict2;
    ArrayList<Word> enWords;
    ArrayList<Word> ruWords;
    ArrayList<Word> wordsForQ;
    Integer controlId = 0, qCount = 0;
    LinearLayout ll;
    TextView tv, t, bar;
    CountDownTimer cdt;
    long time;
    java.lang.String dot = "";
    long timeUntil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survival_layout);
        Log.i(TAG, "Activity launched");

        dict = new Dictionary();
        enWords = dict.getEnWords();
        ruWords = dict.getRuWords();
        dict2 = new Dictionary();
        wordsForQ = dict2.getEnWords();

        ll = (LinearLayout) findViewById(R.id.ll);
        tv = (TextView) findViewById(R.id.q);
        t = (TextView) findViewById(R.id.timeLeftView);
        bar = (TextView) findViewById(R.id.bar);
        showNewQ();

        time = 3000;
        startTimer(time);


    }

    @Override
    public void onClick(View v) {
        checkAnswer(v);
    }



    public void startTimer (long time) {
        cdt = new CountDownTimer(time, 100) {

            public void onTick(long millisUntilFinished) {

                long seconds = millisUntilFinished / 1000;
                t.setText(java.lang.String.valueOf(seconds));
                Long dotsLeft = millisUntilFinished/200;

                for (int i =0; i<dotsLeft; i++) {
                    dot = dot + ".";
                }
                bar.setText("");
                bar.setText(dot);
                dot = "";
                timeUntil = millisUntilFinished;

            }

            public void onFinish() {
                t.setText("0");
                ll.removeAllViews();
                tv.setText("TIME IS UP!");
                createRestartButton();

            }

        };
        cdt.start();
    }

    public void createRestartButton() {
        Button restartButton = new Button(this);
        restartButton.setText("Restart");
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        ll.addView(restartButton);
    }

    public void showNewQ() {
        Collections.shuffle(wordsForQ);
        ArrayList<Word> wordsForButtons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            wordsForButtons.add(enWords.get(i));
        }
        wordsForButtons.add(wordsForQ.get(0));

        tv.setText(wordsForQ.get(0).getWord());
        Collections.shuffle(wordsForButtons);
        controlId = wordsForQ.get(0).getId();
        Log.i(TAG, "correctWordArrIndex is now " + controlId);
        for (int i = 0; i < 4; i++) {
            Button bt = new Button(this);
            bt.setText(ruWords.get(wordsForButtons.get(i).getId()).getWord());
            bt.setId(wordsForButtons.get(i).getId());
            bt.setOnClickListener(this);
            ll.addView(bt);
        }
    }
    public void checkAnswer(View v) {
        // Log.i(TAG, "SOME BuTTON CLICKED!");
        if (controlId == v.getId()) {
            Log.i(TAG, "-- C O R R E C T --");
            qCount++;
            Log.i(TAG, "answeredQuestions is now " + qCount);
            wordsForQ.remove(0);
            cdt.cancel();
            time = timeUntil+1500;
            startTimer(time);

            ll.removeAllViews();
            if (qCount != enWords.size()) {
                showNewQ();
            } else {
                Log.i(TAG, "-- W I N ! --");
                cdt.cancel();
                tv.setText("YOU WIN!");
                createRestartButton();
            }

        } else {
            Log.i(TAG, "-- S O R R Y, B Y E ! --");
            ll.removeAllViews();
            tv.setText(wordsForQ.get(0).getWord() + " = " + ruWords.get(wordsForQ.get(0).getId()).getWord());
            cdt.cancel();
            createRestartButton();
        }

    }

}
