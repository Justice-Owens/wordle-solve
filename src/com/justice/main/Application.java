package com.justice.main;

import java.util.HashMap;
import java.util.Scanner;

public class Application {

    public static void main(String args[]){
        Scanner userIn = new Scanner(System.in);
        WordList wordList = new WordList();
        HashMap<String, Integer> letterMap = wordList.letterCount();

        WordSolve solve = new WordSolve(letterMap);
        String[] answer = solve.getAnswer(solve.getTopLetters());

        System.out.print("First guess: ");
        for(String s: answer){
            System.out.print(s);
        }
        System.out.println();

        System.out.println("Please enter any letters that were not correct separated by commas with no spaces: ");
        String[] incorrectLetters = userIn.nextLine().split(",");

        solve.removePossibleLetters(incorrectLetters);

        answer = solve.getAnswer(solve.getTopLetters());
        System.out.println("Second Guess: ");
        for(String s: answer){
            System.out.print(s);
        }
    }
}
