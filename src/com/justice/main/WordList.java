package com.justice.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @Author JusticeOwens
 *
 * This class handles the creation of the file I/O related to creating the 5-letter-words.txt file and counting each letters
 * occurrence for all 5-letter words
 */
public class WordList {
    private File dictionary = new File("resources/words_alpha.txt");
    private File wordList = new File("resources/5-letter-words.txt");
    private File countOfLetters = new File("resources/letter-count.txt");

    //CONSTRUCTOR
    public WordList() {
        //Checks to see if file exists or is empty.
        try(Scanner fileCheck = new Scanner(wordList)){
            //Exists but empty. Runs WordListSetup()
            if(!fileCheck.hasNextLine()){
                WordlistSetup();
            }
            //Does not exist and runs WordListSetup()
        } catch(FileNotFoundException e){
            System.out.println("5 letter word list not found.");
            WordlistSetup();
        }
    }

    /**
     * Creates the 5-letter words file if it doesn't exist in the proper folder using the words_alpha.txt file
     *
     * @returns null
     */
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

    /**
     * Reads all 5-letter words from the .txt file and counts each letters occurrence throughout the whole file.
     *
     * @return Hashmap containing Char A-Z as the key and count as the value
     */
    public HashMap<String, Integer> LetterCount(){
        System.out.println("Counting letters of all words in " + wordList.toPath());

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
