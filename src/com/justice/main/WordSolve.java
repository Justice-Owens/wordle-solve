package com.justice.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordSolve {
    private HashMap<String, Integer> letterMap;
    private HashMap<String, ArrayList<Integer>> wrongPositionIndex = new HashMap<>();
    private HashMap<String, ArrayList<Integer>> correctPositionIndex = new HashMap<>();
    private ArrayList<String> possibleWords = new ArrayList<>();
    List<String> topLetters = new ArrayList<>();
    private int counter = 0;
    File wordList = new File("resources/words.txt");

    public WordSolve(HashMap<String, Integer> letterMap) {
        this.letterMap = letterMap;
        try(Scanner wordFileIn = new Scanner(wordList)){
            while(wordFileIn.hasNextLine()){
                possibleWords.add(wordFileIn.nextLine().toUpperCase());
            }
        } catch (FileNotFoundException e){
            System.err.println(e.getMessage());
        }
    }

    public List<String> getTopLetters(){
        resetTopLetters();

        if(counter == 0 || counter == 1) {
            for (Map.Entry<String, Integer> entry : letterMap.entrySet()) {
                if(correctPositionIndex.containsKey(entry.getKey()) || wrongPositionIndex.containsKey(entry.getKey())) continue;

                for (int i = 0; i < 5; i++) {
                    if (letterMap.get(topLetters.get(i)) < entry.getValue()) {
                        topLetters.set(i, entry.getKey());
                        break;
                    }
                }
            }
        } else if(wrongPositionIndex.size() == 5) {
            for(Map.Entry<String, ArrayList<Integer>> entry: wrongPositionIndex.entrySet()) topLetters.add(entry.getKey());

        } else {
            for (Map.Entry<String, Integer> entry : letterMap.entrySet()) {
                for(int i = 0; i < 5; i++){
                    if (letterMap.get(topLetters.get(i)) < entry.getValue()) {
                        topLetters.set(i, entry.getKey());
                        break;
                    }
                }
            }
            if(!correctPositionIndex.isEmpty()){
                for(Map.Entry<String, ArrayList<Integer>> entry: correctPositionIndex.entrySet()){
                    if(topLetters.contains(entry.getKey()) && entry.getValue().size() > 1) {
                        Collections.sort(entry.getValue());
                        for (Integer i: correctPositionIndex.get(entry.getKey())){
                            topLetters.remove(entry.getKey());
                            topLetters.add(i, entry.getKey());
                        }
                    } else if (topLetters.contains(entry.getKey())) {
                        topLetters.remove(entry.getKey());
                        topLetters.add(entry.getValue().get(0), entry.getKey());
                    }
                }
            }
//            if(!wrongPositionIndex.isEmpty()){
//                for(Map.Entry<String, ArrayList<Integer>> entry: wrongPositionIndex.entrySet()){
//                    if(topLetters.contains(entry.getKey())){
//                        for(Integer i: entry.getValue()){
//                            if(topLetters.indexOf(entry.getKey()) == i){
//                                topLetters.remove(entry.getKey());
//                                topLetters.add(i, entry.getKey());
//                            }
//                        }
//                    }
//                }
//            }
        }

        return topLetters;
    }

    public void removePossibleWordsAndLetters(String[] incorrectLetters){

        mainloop:
        for(int i = 0; i < possibleWords.size(); ){
            for(String l: incorrectLetters){
                if(possibleWords.get(i).contains(l)) {
                    possibleWords.remove(possibleWords.get(i));
                    continue mainloop;
                }
            }
            i++;
        }
        for(String l: incorrectLetters){
            letterMap.remove(l);
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
        String user;
        String[] correctPosition;
        List<String> letters = getTopLetters();
        List<String> answer = getAnswer(letters);

        while(answer.isEmpty()) answer = findNewAnswer(letters);

        if(counter == 6){
            System.out.println("All guesses used!");
            return;
        }

        System.out.println("Guess " + (counter + 1) + ": ");
        for(String s: answer){
            System.out.print(s);
        }
        System.out.println("Which letters were incorrect: ");
        user = userIn.nextLine();
        if(!user.isBlank())  {
            wrongLetters = user.split(",");
            removePossibleWordsAndLetters(wrongLetters);
            user = "";
        }


        System.out.println("Which letters were correct and in the wrong position?");
        user = userIn.nextLine();
        if(!user.isBlank()) {
            wrongPosition = user.split(",");
            for(String s: wrongPosition){
                if(!wrongPositionIndex.containsKey(s)) wrongPositionIndex.put(s, new ArrayList<>(answer.indexOf(s)));
                else wrongPositionIndex.get(s).add(answer.indexOf(s));
            }
            user = "";
        }



        System.out.println("Which letters were in the correct position?");
        user = userIn.nextLine();
        if(!user.isBlank()) {
            correctPosition = user.split(",");
            for(String s: correctPosition){
                if(!correctPositionIndex.containsKey(s)) correctPositionIndex.put(s, new ArrayList<>(Arrays.asList(answer.indexOf(s))));
                else correctPositionIndex.get(s).add(answer.indexOf(s));
            }
        }


        counter++;
        solve();

    }

    private List<String> findNewAnswer(List<String> oldLetters) {
        List<String> unusedLetters = new ArrayList<>();
        for(Map.Entry<String, Integer> entry: letterMap.entrySet()) unusedLetters.add(entry.getKey());

        unusedLetters.removeAll(oldLetters);
        unusedLetters.sort(Comparator.comparingInt(letterMap::get).reversed());

        for(int i = 0; i < topLetters.size(); i++){
            String currentLetter = topLetters.get(i);
            int currentCount = letterMap.get(currentLetter);

            if(currentCount < letterMap.get(unusedLetters.get(0))){
                topLetters.set(i, unusedLetters.remove(0));
            }
        }

        return getAnswer(topLetters);
    }

//    public int indexOf(List<String> array, String key){
//        for(int i = 0; i < array.size(); i++){
//            if(key.equalsIgnoreCase(array.get(i))){
//                return i;
//            }
//        }
//        return -1;
//    }

    public boolean wrongIndexCheck(String letter, int index){
        return (wrongPositionIndex.get(letter).contains(index));

    }

    public void resetTopLetters(){
        topLetters.clear();
        String lowestLetter = "";
        int lowestOccurrence = 999999;

        for(Map.Entry<String, Integer> entry: letterMap.entrySet()){
            if(entry.getValue() < lowestOccurrence){
                lowestLetter = entry.getKey();
                lowestOccurrence = entry.getValue();
            }
        }

        for(int i = 0; i < 5; i++){
            topLetters.add(lowestLetter);
        }
    }
}
