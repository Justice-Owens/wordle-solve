package com.justice.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordSolve {
    private final HashMap<String, Integer> letterMap;
    private final HashMap<String, ArrayList<Integer>> wrongPositionIndex = new HashMap<>();
    private final HashMap<String, ArrayList<Integer>> correctPositionIndex = new HashMap<>();
    private final ArrayList<String> possibleWords = new ArrayList<>();
    List<String> topLetters = new ArrayList<>();
    List<String> letters;
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
        }

        return topLetters;
    }

    public void removePossibleWordsAndLetters(List<String> incorrectLetters){

        mainLoop:
        for(int i = 0; i < possibleWords.size(); ){
            for(String l: incorrectLetters){
                if(possibleWords.get(i).contains(l)) {
                    possibleWords.remove(possibleWords.get(i));
                    continue mainLoop;
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
        List<String> wrongLetters;
        List<String> wrongPosition;
        String user;
        String[] correctPosition;


        while(counter < 6) {

            letters = getTopLetters();
            List<String> answer = getAnswer(letters);

            if(correctPositionIndex.size() >= 3 || possibleWords.size() < 20){
                answer = searchWordsWithCorrectIndex();
            }

            while (answer.isEmpty()) {
                answer = findNewAnswer(letters.get(4));
                if (!answer.isEmpty()) break;
            }

            System.out.println("Guess " + (counter + 1) + ": ");
            for (String s : answer) {
                System.out.print(s);
            }
            System.out.println("\nWhich letters were incorrect: ");
            user = userIn.nextLine();
            if (!user.isBlank()) {
                List<String> updatedWrongLetters = new ArrayList<>();
                wrongLetters = List.of(user.toUpperCase().split(""));
                for(String s: wrongLetters){
                    if(!correctPositionIndex.containsKey(s)){
                        updatedWrongLetters.add(s);
                    }
                }
                removePossibleWordsAndLetters(updatedWrongLetters);
            }


            System.out.println("\nWhich letters were correct and in the wrong position?");
            user = userIn.nextLine();
            if (!user.isBlank()) {
                wrongPosition = List.of(user.toUpperCase().split(""));
                for (String s : wrongPosition) {
                    if (!wrongPositionIndex.containsKey(s)) wrongPositionIndex.put(s, new ArrayList<>(List.of(answer.indexOf(s))));
                    else if(!wrongPositionIndex.get(s).contains(answer.indexOf(s))) wrongPositionIndex.get(s).add(answer.indexOf(s));
                }
                removeWordsWithLettersAtIncorrectIndex(wrongPosition, answer);
                removeWordsNotContainingIncorrectIndex(wrongPosition);
            }


            System.out.println("\nWhich letters were in the correct position?");
            user = userIn.nextLine();
            if (!user.isBlank()) {
                correctPosition = user.toUpperCase().split("");
                for (String s : correctPosition) {
                    if (!correctPositionIndex.containsKey(s)) {
                        correctPositionIndex.put(s, new ArrayList<>(List.of(answer.indexOf(s))));
                        removeWordsNotContainingCorrectPositionIndex(s, answer.indexOf(s));
                        wrongPositionIndex.remove(s);
                    }
                    else if (!correctPositionIndex.get(s).contains(answer.indexOf(s))) correctPositionIndex.get(s).add(answer.indexOf(s));
                }

            }

            crossCheckWrongAndCorrectIndexes();
            counter++;
        }
        System.out.println("All guesses used!");
    }

    private void removeWordsNotContainingIncorrectIndex(List<String> wrongPosition) {
        String word;

        for(String s: wrongPosition){
            for(int i = 0; i < possibleWords.size();){
                word = possibleWords.get(i);
                if(!word.contains(s)) possibleWords.remove(possibleWords.get(i));
                else i++;
            }
        }
    }

    private void removeWordsWithLettersAtIncorrectIndex(List<String> letters, List<String> answer) {
        int index;

        for(String s: letters){
            index = answer.indexOf(s);

            for(int i = 0; i < possibleWords.size(); ){
                if (possibleWords.get(i).substring(index, index + 1).equalsIgnoreCase(s)) possibleWords.remove(possibleWords.get(i));
                else i++;
            }
        }
    }

    private void removeWordsNotContainingCorrectPositionIndex(String letter, int index) {
        List<String> stringArray;
        for(int i = 0; i < possibleWords.size(); ){
            stringArray = List.of(possibleWords.get(i).split(""));

            if(!stringArray.get(index).equalsIgnoreCase(letter))possibleWords.remove(possibleWords.get(i));
            else i++;

        }
    }

    private void crossCheckWrongAndCorrectIndexes() {

        for(Map.Entry<String, ArrayList<Integer>> entry: wrongPositionIndex.entrySet()){
            ArrayList<Integer> possiblePositions = new ArrayList<>(Arrays.asList(0,1,2,3,4));

            possiblePositions.removeAll(entry.getValue());

            if(correctPositionIndex.size() >= 3) for(Map.Entry<String, ArrayList<Integer>> correct: correctPositionIndex.entrySet()) possiblePositions.removeAll(correct.getValue());

            if(possiblePositions.size() == 1){
                if(correctPositionIndex.containsKey(entry.getKey()) && !correctPositionIndex.get(entry.getKey()).contains(possiblePositions.get(0))){
                    correctPositionIndex.put(entry.getKey(), new ArrayList<>(List.of(possiblePositions.get(0))));
                }
            }
        }
    }

    private List<String> searchWordsWithCorrectIndex() {

        for(String s: possibleWords){
            int correctPositionCounter = 0;
            List<String> wordArray = Arrays.asList(s.split(""));

            for(int i = 0; i < wordArray.size(); i++){
                if((correctPositionIndex.containsKey(wordArray.get(i)) && correctPositionIndex.get(wordArray.get(i)).contains(i) || (wrongPositionIndex.containsKey(wordArray.get(i)) &&
                        !wrongPositionIndex.get(wordArray.get(i)).contains(i)))){
                    if(wrongPositionIndex.containsKey(wordArray.get(i)) && wrongPositionIndex.get(wordArray.get(i)).contains(i)); //Do nothing
                    else correctPositionCounter++;
                }
            }
            if(correctPositionCounter >= correctPositionIndex.size()){
                return wordArray;
            }
        }
        return null;
    }

    private List<String> searchRemainingWords() {
        List<String> possibleWordArray;
        List<String> answer = new ArrayList<>();

        mainLoop:
        for(String s: possibleWords){
            answer.clear();
            if(correctPositionIndex.isEmpty()){
                possibleWordArray = (Arrays.asList(s.split("")));
                for(int i = 0; i < possibleWordArray.size(); i++){
                    if(wrongPositionIndex.containsKey(possibleWordArray.get(i)) && wrongPositionIndex.get(possibleWordArray.get(i)).contains(i)){
                        continue mainLoop;
                    } else {
                        if(correctPositionIndex.containsKey(possibleWordArray.get(i)) && correctPositionIndex.get(possibleWordArray.get(i)).contains(i)){

                        }
                    }
                }
                return answer;
            }
        }
        return answer;
    }

    private List<String> findNewAnswer(String oldLetter) {
        List<String> unusedLetters = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : letterMap.entrySet()) unusedLetters.add(entry.getKey());
        unusedLetters.sort(Comparator.comparingInt(letterMap::get).reversed());

        if(!correctPositionIndex.containsKey(oldLetter) && !wrongPositionIndex.containsKey(oldLetter)) {
            unusedLetters.remove(oldLetter);
            letters.remove(oldLetter);
        }

        while(getAnswer(letters).isEmpty()) {
            for (int i = 0; i < topLetters.size(); i++) {
                String currentLetter = letters.get(i);
                int currentCount = letterMap.get(currentLetter);

                if (currentCount < letterMap.get(unusedLetters.get(0))) {
                    letters.set(i, unusedLetters.remove(0));
                }
            }
            if(unusedLetters.isEmpty()) return searchRemainingWords();
        }
        return getAnswer(letters);
    }

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
