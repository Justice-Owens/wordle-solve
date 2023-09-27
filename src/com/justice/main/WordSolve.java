package com.justice.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordSolve {
    private HashMap<String, Integer> letterMap;
    private HashMap<String, ArrayList<Integer>> wrongPositionIndex = new HashMap<>();
    private HashMap<String, ArrayList<Integer>> correctPositionIndex = new HashMap<>();
    private ArrayList<String> possibleWords = new ArrayList<>();
    private int counter = 0;
    File wordList = new File("resources/words.txt");

    public WordSolve(HashMap<String, Integer> letterMap) {
        this.letterMap = letterMap;
        try(Scanner wordFileIn = new Scanner(wordList)){
            while(wordFileIn.hasNextLine()){
                possibleWords.add(wordFileIn.nextLine());
            }
        }catch (FileNotFoundException e){
            System.err.println(e.getMessage());
        }
    }

    public List<String> getTopLetters(){
        List<String> topLetters = new ArrayList<>();

        if(counter == 0 || counter == 1) {
            for (Map.Entry<String, Integer> entry : letterMap.entrySet()) {
                if(correctPositionIndex.containsKey(entry.getKey())) continue;

                for (int i = 0; i < 5; i++) {
                    if(topLetters.isEmpty()) topLetters.add(entry.getKey());
                    else if (letterMap.get(topLetters.get(i)) < entry.getValue()) topLetters.set(i, entry.getKey());
                }
            }
        } else if(wrongPositionIndex.size() == 5) {
            int index = 0;
            for(Map.Entry<String, ArrayList<Integer>> entry: wrongPositionIndex.entrySet()){
                topLetters.set(index, entry.getKey());
                index++;
            }
        } else {
            for (Map.Entry<String, Integer> entry : letterMap.entrySet()) {

            }
        }

        return topLetters;
    }

    public void removePossibleWordsAndLetters(String[] incorrectLetters){

        for(String s: possibleWords){
            for(String l: incorrectLetters){
                if(s.contains(l)) possibleWords.remove(s);
                letterMap.remove(l);
            }
        }
    }

    public List<String> getAnswer(List<String> topLetters){
        List<String> answer = new ArrayList<>();
        String word;
        List<String> wordArray;

        try (Scanner listInput = new Scanner(wordList)) {

            if(wrongPositionIndex.isEmpty() || counter == 1) {
                Collections.sort(topLetters);

                for(String s: possibleWords){
                    word = s.toUpperCase();

                    wordArray = Arrays.asList(word.split(""));
                    Collections.sort(wordArray);

                    if (wordArray.containsAll(topLetters))  return Arrays.asList(word.split(""));
                }
            } else {
                mainLoop:
                while(listInput.hasNextLine()){
                    word = listInput.nextLine().toUpperCase();
                    wordArray = Arrays.asList(word.split(""));

                    for(int i = 0; i < wordArray.size(); i++){
                        if(wrongPositionIndex.containsKey(wordArray.get(i))){
                            if(wrongIndexCheck(wordArray.get(i), i)){
                                continue mainLoop;
                            }
                        }
                    }
                    if(wordArray.containsAll(topLetters)) return Arrays.asList(word.split(""));
                }

            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        return answer;
    }

    public void solve(){
        Scanner userIn = new Scanner(System.in);
        String[] wrongLetters;
        String[] wrongPosition;
        String[] correctPosition;
        List<String> letters = getTopLetters();
        List<String> answer = getAnswer(letters);

        if(counter == 6){
            System.out.println("All guesses used!");
            return;
        }

        System.out.println("Guess " + counter + 1 + ": ");
        for(String s: answer){
            System.out.print(s);
        }
        System.out.println("Which letters were incorrect: ");
        wrongLetters = userIn.nextLine().split(",");

        removePossibleWordsAndLetters(wrongLetters);

        System.out.println("Which letters were correct and in the wrong position?");
        wrongPosition = userIn.nextLine().split(",");

        for(String s: wrongPosition){
            if(!wrongPositionIndex.containsKey(s)) wrongPositionIndex.put(s, new ArrayList<>(indexOf(answer, s)));
            else wrongPositionIndex.get(s).add(indexOf(answer,s));
        }

        System.out.println("Which letters were in the correct position?");
        correctPosition = userIn.nextLine().split(",");

        for(String s: correctPosition){
            if(!correctPositionIndex.containsKey(s)) correctPositionIndex.put(s, new ArrayList<>(indexOf(answer,s)));
            else correctPositionIndex.get(s).add(indexOf(answer,s));
        }

        solve();

    }

    public int indexOf(List<String> array, String key){
        for(int i = 0; i < array.size(); i++){
            if(key.equalsIgnoreCase(array.get(i))){
                return i;
            }
        }
        return -1;
    }

    public boolean wrongIndexCheck(String letter, int index){
        return (wrongPositionIndex.get(letter).contains(index));

    }
}
