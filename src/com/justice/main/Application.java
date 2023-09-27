package com.justice.main;

import java.util.HashMap;
import java.util.Scanner;

public class Application {

    public static void main(String args[]){
        WordList wordList = new WordList();
        HashMap<String, Integer> letterMap = wordList.letterCount();

        WordSolve solve = new WordSolve(letterMap);
        solve.solve();
    }
}
