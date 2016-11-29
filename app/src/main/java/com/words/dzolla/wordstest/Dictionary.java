package com.words.dzolla.wordstest;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by eee on 04.11.2016.
 */
public class Dictionary {
    public static final String TAG = "MyApp";

    private ArrayList<Word> enWords = new ArrayList<>();
    private ArrayList<Word> ruWords = new ArrayList<>();
    private String[] enSignatures = {"cat", "dog", "table", "lamp", "apple", "bag",
    "phone", "horse", "happy", "kind", "mouse", "bottle", "hair", "food", "vinegar",
    "soul", "life", "lie", "money", "fame"};
    private String[] ruSignatures = {"кот", "собака", "стол", "лампа", "яблоко", "сумка",
    "телефон", "лошадь", "счастливый", "добрый", "мышь", "бутылка", "волосы", "еда", "уксус",
            "душа", "жизнь", "ложь", "деньги", "слава"};
    private Integer length = enSignatures.length;

    public Dictionary() {
        Log.i(TAG, "enSignatures length = " + length);
        for (int i = 0; i < enSignatures.length; i++) {
            enWords.add(new Word(i, enSignatures[i]));
            ruWords.add(new Word(i, ruSignatures[i]));

        }
    }

    public ArrayList<Word> getEnWords() {
        return enWords;
    }

    public ArrayList<Word> getRuWords() {
        return ruWords;
    }

}
