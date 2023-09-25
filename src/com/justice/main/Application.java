package com.justice.main;

import java.util.HashMap;

public class Application {

    public static void main(String args[]){
        WordList wordList = new WordList();
        HashMap<String, Integer> wordMap = wordList.LetterCount();

        WordSolve solve = new WordSolve(wordMap);


    }
}
