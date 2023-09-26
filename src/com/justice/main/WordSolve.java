package com.justice.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordSolve {
    private HashMap<String, Integer> letterMap;
    File wordList = new File("resources/words.txt");

    public WordSolve(HashMap<String, Integer> letterMap) {
        this.letterMap = letterMap;
    }

    public String[] getTopLetters(){
        String[] topLetters = new String[]{"Q", "Q", "Q", "Q", "Q"};

        for(Map.Entry<String, Integer> entry: letterMap.entrySet()) {
                loop: for (int i = 0; i < 5; i++) {
                    if (letterMap.get(topLetters[i]) < entry.getValue())
                        switch (i) {
                            case 0 -> {
                                topLetters[i + 4] = topLetters[i + 3];
                                topLetters[i + 3] = topLetters[i + 2];
                                topLetters[i + 2] = topLetters[i + 1];
                                topLetters[i + 1] = topLetters[i];
                                topLetters[i] = entry.getKey();
                                break loop;
                            }
                            case 1 -> {
                                topLetters[i + 3] = topLetters[i + 2];
                                topLetters[i + 2] = topLetters[i + 1];
                                topLetters[i + 1] = topLetters[i];
                                topLetters[i] = entry.getKey();
                                break loop;
                            }
                            case 2 -> {
                                topLetters[i + 2] = topLetters[i + 1];
                                topLetters[i + 1] = topLetters[i];
                                topLetters[i] = entry.getKey();
                                break loop;
                            }
                            case 3 -> {
                                topLetters[i + 1] = topLetters[i];
                                topLetters[i] = entry.getKey();
                                break loop;
                            }
                            case 4 -> topLetters[i] = entry.getKey();
                        }
                    }
                }
        return topLetters;
    }

    public void removePossibleLetters(String[] incorrectLetters){

        for(String s: incorrectLetters){
            letterMap.remove(s);
        }
    }

    public String[] getAnswer(String[] topLetters){
        String[] answer = new String[5];
        Arrays.sort(topLetters);

        try(Scanner listInput = new Scanner(wordList)){
            String word;

            while(listInput.hasNextLine()){
                word = listInput.nextLine().toUpperCase();

                String[] wordArray = word.split("");
                Arrays.sort(wordArray);

                if(Arrays.equals(wordArray, topLetters)){
                    answer = word.split("");
                    return answer;
                }
            }

        } catch (FileNotFoundException e){
            System.err.println(e.getMessage());
        }

        return answer;
    }
}
