package com.justice.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordSolve {
    private HashMap<String, Integer> letterMap;
    File wordList = new File("resources/5-letter-words.txt");

    public WordSolve(HashMap<String, Integer> letterMap) {
        this.letterMap = letterMap;
    }

    public String[] WordGuess(){
        String[] answer = new String[5];
        String[] topLetters = new String[5];

        for(Map.Entry<String, Integer> entry: letterMap.entrySet()){
            if(topLetters[0].isBlank()){
                topLetters[0] = entry.getKey();
            }
            for(int i = 0; i < 5; i++){
                if(letterMap.get(topLetters[i]) < entry.getValue())
                switch(i) {
                    case 0 -> {
                        topLetters[i + 4] = topLetters[i + 3];
                        topLetters[i + 3] = topLetters[i + 2];
                        topLetters[i + 2] = topLetters[i + 1];
                        topLetters[i + 1] = topLetters[i];
                        topLetters[i] = entry.getKey();
                    }
                    case 1 -> {
                        topLetters[i+3]  = topLetters[i+2];
                        topLetters[i + 2] = topLetters[i + 1];
                        topLetters[i + 1] = topLetters[i];
                        topLetters[i] = entry.getKey();
                    }
                    case 2 -> {
                        topLetters[i + 2] = topLetters[i + 1];
                        topLetters[i + 1] = topLetters[i];
                        topLetters[i] = entry.getKey();
                    }
                    case 3 -> {
                        topLetters[i + 1] = topLetters[i];
                        topLetters[i] = entry.getKey();
                    }
                    case 4 -> topLetters[i] = entry.getKey();
                }
            }
        }

        for(String s: topLetters){
            letterMap.remove(s);
        }

        try(Scanner listInput = new Scanner(wordList)){


        } catch (FileNotFoundException e){
            System.err.println(e.getMessage());
        }

        return answer;
    }
}
