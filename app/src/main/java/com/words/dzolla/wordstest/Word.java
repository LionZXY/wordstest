package com.words.dzolla.wordstest;

/**
 * Created by eee on 04.11.2016.
 */
public class Word {
    private Integer id;
    private String word;

    public Word(Integer id, String word) {
        this.id = id;
        this.word = word;
    }

    public Integer getId() {
        return id;
    }

    public String getWord() {
        return word;
    }


}