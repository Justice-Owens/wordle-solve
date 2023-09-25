package com.justice.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class WordList {
    File dictionary = new File("resources/words_alpha.txt");
    File wordList = new File("resources/5-letter-words.txt");
    File countOfLetters = new File("resources/letter-count.txt");

    public WordList() {
        try(Scanner fileCheck = new Scanner(wordList);){
            if(!fileCheck.hasNextLine()){
                WordlistSetup();
            }
        } catch(FileNotFoundException e){
            System.out.println("5 letter word list not found.");
            WordlistSetup();
        }
    }

    public void WordlistSetup(){
        System.out.println("Creating 5 letter word list...");
        String word;

        try(Scanner fileRead = new Scanner(dictionary);
            PrintWriter printWriter = new PrintWriter(wordList);){

            while(fileRead.hasNextLine()){
                word = fileRead.nextLine();

                if(word.length() == 5 && !word.contains(" ")){
                    printWriter.println(word);
                }
            }

        } catch(FileNotFoundException e){
            System.err.println("File not found. Check that all necessary files are in correct location.");
        }
    }

    public HashMap<String, Integer> LetterCount(){
        if(countOfLetters.exists()){
            countOfLetters.delete();
        }

        HashMap<String, Integer> counterMap = new HashMap<>();

        try(Scanner inputCount = new Scanner(wordList);
            PrintWriter printWriter = new PrintWriter(countOfLetters);){
            String wordToCount;
            String[] wordArray;


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
                printWriter.println(entry.getKey() + "," + entry.getValue());
            }

        } catch (FileNotFoundException e){
            System.err.println(wordList.toPath() + " not found");
        }

        return counterMap;
    }
}
