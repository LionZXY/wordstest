package com.words.dzolla.wordstest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Collections;

public class LevelActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MyApp";

    private ArrayList<Word> origWords, targetWords;
    private Integer correctWordArrIndex = 0, answeredQuestions = 0;
    private LinearLayout linear_layout;
    private TextView textView, timeLeftView, bar;
    private CountDownTimer countDownTimer;
    InterstitialAd mInterstitialAd;

    private long timeUntil;
    private int levelId, courseId, origLangId, targetLangId;
    private static final int NUMBER_OF_WRONG_BUTTONS = 1;
    private static final int INITIAL_TIME_MILLISECONDS = 6000;
    private static final int ADD_TIME_MILLISECONDS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survival_layout);
        getIntents(); //получаем нужные нам айдишники для запросов

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                createRestartButton();
//                beginPlayingGame();
            }
        });

        requestNewInterstitial();

        // выбрать из базы все слова, относящиеся к этому левелу, добавить их в список
        DBHelper mDBHelper = new DBHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        String[] origWordsColumns = getWordsColumns(origLangId);
        Cursor origWordsCursor = db.query("words", origWordsColumns, "words.level=" + levelId, null, null, null, null);
        origWords = putWordsToArrayList(origWordsCursor); //word.Id = ArrayList.index

        String[] targetWordsColumns = getWordsColumns(targetLangId);
        Cursor targetWordsCursor = db.query("words", targetWordsColumns, "words.level=" + levelId, null, null, null, null);
        targetWords = putWordsToArrayList(targetWordsCursor); //word.Id = ArrayList.index

        Collections.shuffle(targetWords);
        Log.i(TAG, "targetWords shuffled");

        linear_layout = (LinearLayout) findViewById(R.id.ll);
        textView = (TextView) findViewById(R.id.q);
        timeLeftView = (TextView) findViewById(R.id.timeLeftView);
        bar = (TextView) findViewById(R.id.bar);

        showNewQ();

        startTimer(INITIAL_TIME_MILLISECONDS);
    }

    @Override
    public void onClick(View v) {
        checkAnswer(v);
    }

    public void startTimer(long time) {
        countDownTimer = new CountDownTimer(time, 100) {

            public void onTick(long millisUntilFinished) {

                long seconds = millisUntilFinished / 1000;
                timeLeftView.setText(java.lang.String.valueOf(seconds));
                Long dotsLeft = millisUntilFinished / 200;
                String dot = "";
                for (int i = 0; i < dotsLeft; i++) {
                    dot = dot + "||";
                }
                bar.setText("");
                bar.setText(dot);
                timeUntil = millisUntilFinished;
            }

            public void onFinish() {
                timeLeftView.setText("0");
                linear_layout.removeAllViews();
                textView.setText("TIME IS UP!");
                if (mInterstitialAd.isLoaded()) {
                    Log.i(TAG, "Interstitial ad is loaded");
                    mInterstitialAd.show();
                } else {
                createRestartButton();}
            }
        };
        countDownTimer.start();
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
        linear_layout.addView(restartButton);
    }

    public void showNewQ() {
        textView.setText(targetWords.get(correctWordArrIndex).getWord());
        Log.i(TAG, "correctWordArrIndex is now " + correctWordArrIndex);

        java.lang.String[] origWordsColumns = getWordsColumns(origLangId);

        DBHelper mDBHelper = new DBHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor origWordsCursor = db.query("words", origWordsColumns, "words.level=" + levelId, null, null, null, null);
        ArrayList<Word> tempOrigWords;
        Log.i(TAG, "temp_OrigWords:");
        tempOrigWords = putWordsToArrayList(origWordsCursor); //

        Log.i(TAG, "-------id for removal is " + targetWords.get(correctWordArrIndex).getId());
        tempOrigWords.remove((int) targetWords.get(correctWordArrIndex).getId());
        Log.i(TAG, "tempOrigWords after removal: ");
        for (int i = 0; i < tempOrigWords.size(); i++) {
            Log.i(TAG, tempOrigWords.get(i).getWord());
        }

        Collections.shuffle(tempOrigWords);
        Log.i(TAG, "tempOrigWords after shuffle: ");
        for (int i = 0; i < tempOrigWords.size(); i++) {
            Log.i(TAG, tempOrigWords.get(i).getWord());
        }

        ArrayList<Word> wordsForButtons = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_WRONG_BUTTONS; i++) {
            wordsForButtons.add(tempOrigWords.get(i));
        }

        wordsForButtons.add(origWords.get(targetWords.get(correctWordArrIndex).getId()));

        Collections.shuffle(wordsForButtons);

        for (int i = 0; i < NUMBER_OF_WRONG_BUTTONS + 1; i++) {
            Button bt = new Button(this);
            bt.setText(wordsForButtons.get(i).getWord());
            bt.setId(wordsForButtons.get(i).getId());
            bt.setOnClickListener(this);
            linear_layout.addView(bt);
        }
    }

    public void checkAnswer(View v) {
        // Log.i(TAG, "SOME BuTTON CLICKED!");
        if (targetWords.get(correctWordArrIndex).getId() == v.getId()) {
            Log.i(TAG, "-- C O R R E C T --");
            playSound(R.raw.correct);
            answeredQuestions++;
            correctWordArrIndex++;
            Log.i(TAG, answeredQuestions + " questions answered");
            countDownTimer.cancel();
            long time = timeUntil + ADD_TIME_MILLISECONDS;
            startTimer(time);

            linear_layout.removeAllViews();
            if (answeredQuestions != origWords.size()) {
                showNewQ();
            } else {
                Log.i(TAG, "-- W I N ! --");
                countDownTimer.cancel();
                textView.setText("YOU WIN!");
                createRestartButton();
            }

        } else {
            Log.i(TAG, "-- S O R R Y, B Y E ! --");
            linear_layout.removeAllViews();
            textView.setText(targetWords.get(correctWordArrIndex).getWord() +
                    " = " + origWords.get((targetWords.get(correctWordArrIndex).getId())).getWord());
            countDownTimer.cancel();
            createRestartButton();
        }
    }

    private java.lang.String[] getWordsColumns(int langId) {
        java.lang.String[] wordsColumns = new java.lang.String[2];
        wordsColumns[0] = "_id";
        if (langId == 1) {
            wordsColumns[1] = "en_word";
        } else if (langId == 2) {
            wordsColumns[1] = "ru_word";
        } else if (langId == 3) {
            wordsColumns[1] = "de_word";
        }
        return wordsColumns;
    }

    private ArrayList<Word> putWordsToArrayList(Cursor c) {
        ArrayList<Word> words = new ArrayList<>();
        if (c.moveToFirst()) {
            int wordId = 0;
            do {
                java.lang.String word = c.getString(1);
                words.add(new Word(wordId, word));
                Log.i(TAG, wordId + " " + word);
                wordId++;
            } while (c.moveToNext());

        } else {
            Log.i(TAG, "table is empty");
        }
        c.close();
        return words;
    }

    private void getIntents() {
        courseId = getIntent().getIntExtra("courseId", -1);
        origLangId = getIntent().getIntExtra("origLangId", -1);
        Log.i(TAG, "origLangId = " + origLangId);
        targetLangId = getIntent().getIntExtra("targetLangId", -1);
        Log.i(TAG, "targetLangId = " + targetLangId);
        levelId = getIntent().getIntExtra("levelId", -1);
    }

    private void playSound(Integer soundResId) {
        try {
            MediaPlayer mp = MediaPlayer.create(this, soundResId);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.i(TAG, "mp released");
                    mediaPlayer.release();
                }
            });
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
}
