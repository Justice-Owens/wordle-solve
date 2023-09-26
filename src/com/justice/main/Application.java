package com.justice.main;

import java.util.HashMap;

public class Application {

    public static void main(String args[]){
        WordList wordList = new WordList();
        HashMap<String, Integer> letterMap = wordList.letterCount();

        WordSolve solve = new WordSolve(letterMap);
        String[] answer = solve.getAnswer(solve.getTopLetters());

        for(String s: answer){
            System.out.print(s);
        }

    }
}
