package com.justice.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordSolve {
    private HashMap<String, Integer> letterMap;
    private HashMap<String, ArrayList<Integer>> wrongPositionIndex = new HashMap<>();
    private int counter = 0;
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

    public void solve(){
        Scanner userIn = new Scanner(System.in);
        String[] wrongLetters;
        String[] wrongPosition;
        String[] letters = getTopLetters();
        String[] answer = getAnswer(letters);

        if(counter == 6){
            System.out.println("All guesses used!");
            return;
        }

        System.out.println("Guess: ");
        for(String s: answer){
            System.out.print(s);
        }
        System.out.println("Which letters were incorrect: ");
        wrongLetters = userIn.nextLine().split(",");

        removePossibleLetters(wrongLetters);

        System.out.println("Which letters were correct and in the wrong position?");
        wrongPosition = userIn.nextLine().split(",");

        for(String s: wrongPosition){
            if(!wrongPositionIndex.containsKey(s)) {
                wrongPositionIndex.put(s, new ArrayList<>(indexOf(answer, s)));
            } else {
                wrongPositionIndex.get(s).add(indexOf(answer,s));
            }
        }

        solve();

    }

    public int indexOf(String[] array, String key){
        for(int i = 0; i < array.length; i++){
            if(key.equalsIgnoreCase(array[i])){
                return i;
            }
        }
        return -1;
    }
}
