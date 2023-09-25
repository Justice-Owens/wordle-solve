package com.justice.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class WordList {
    File dictionary = new File("resources/dictionary.csv");
    File wordList = new File("resources/5-letter-words.txt");
    File letterCount = new File("resources/letter-count.txt");

    public WordList() {
        try(Scanner fileCheck = new Scanner(wordList);){
            if(!fileCheck.hasNextLine()){
                WordlistSetup();
            }
        } catch(FileNotFoundException e){
            System.out.println("5 letter word list not found.");
            WordlistSetup();
        }

        LetterCount();
    }

    public void WordlistSetup(){
        System.out.println("Creating 5 letter word list...");

        String[] word;
        String dictionaryLine;
        List<String> enteredWords = new ArrayList<>();

        try(Scanner fileRead = new Scanner(dictionary);
            PrintWriter printWriter = new PrintWriter(wordList);){

            while(fileRead.hasNextLine()){
                dictionaryLine = fileRead.nextLine();
                word = dictionaryLine.split(",");
                if(word[0].length() > 3) {
                    word[0] = word[0].substring(1, word[0].length() - 1);
                }

                if(!enteredWords.contains(word[0]) && word[0].length() == 5 && (!word[0].contains(" ") && !word[0].contains("-") && !word[0].contains("'") && !word[0].contains("/"))){
                    enteredWords.add(word[0]);
                    printWriter.println(word[0]);
                }
            }

        } catch(FileNotFoundException e){
            System.err.println("File not found. Check that all necessary files are in correct location.");
        }
    }

    public void LetterCount(){
        if(letterCount.exists()){
            letterCount.delete();
        }

        try(Scanner inputCount = new Scanner(wordList);
            PrintWriter printWriter = new PrintWriter(letterCount);){
            String wordToCount;
            String[] wordArray;

            HashMap<String, Integer> counterMap = new HashMap<>();

            while(inputCount.hasNextLine()){
                wordToCount = inputCount.nextLine();
                wordArray = wordToCount.toUpperCase().split("");

                for(String s: wordArray){
                    if(counterMap.containsKey(s)){
                        counterMap.put(s, counterMap.get(s) + 1);
                    } else {
                        counterMap.put(s, 1);
                    }
                }
            }

            for(Map.Entry<String, Integer> entry: counterMap.entrySet()){
                printWriter.println(entry.getKey() + ": " + entry.getValue());
            }

        } catch (FileNotFoundException e){
            System.err.println(wordList.toPath() + " not found");
        }
    }
}
