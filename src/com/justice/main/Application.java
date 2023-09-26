package com.justice.main;

import java.util.HashMap;

public class Application {

    public static void main(String args[]){
        WordList wordList = new WordList();
        HashMap<String, Integer> letterMap = wordList.LetterCount();

        WordSolve solve = new WordSolve(letterMap);

    }
}
