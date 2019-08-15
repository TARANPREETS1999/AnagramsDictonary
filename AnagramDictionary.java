/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private List<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private HashMap<Integer, ArrayList<String>> sizeToWords;
    private int wordLength = DEFAULT_WORD_LENGTH;
    private static final String TAG = "AnagramDictionary";

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList = new ArrayList<>();
        lettersToWord = new HashMap<>();
        wordSet = new HashSet<>();
        sizeToWords = new HashMap<>();
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            Log.d(TAG, "AnagramDictionary: --------------->" + word);
            wordSet.add(word);
            wordList.add(word);

            String sortedInputWord = sortLetters(word);
            boolean keyExists = lettersToWord.containsKey(sortedInputWord);
            if (keyExists) {
                lettersToWord.get(sortedInputWord).add(word);
            } else {
                ArrayList<String> words = new ArrayList<>();
                words.add(word);
                lettersToWord.put(sortedInputWord, words);
            }

            int length = sortedInputWord.length();

            if (sizeToWords.containsKey(length)) {
                sizeToWords.get(length).add(word);
            } else {
                ArrayList<String> words = new ArrayList<>();
                words.add(word);
                sizeToWords.put(length, words);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    /*public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedInput;
        for(String i:wordList){
            //sortedInput=sortLetters(targetWord);


           *//* if(i.length()!=targetWord.length()){
                continue;
            }else{
                if(sortLetters(i).equals(sortedInput)){*//*
                    //result.add(i);


               *//* }
                else {
                    continue;
                }
            }*//*
        }


        return result;
    }*/

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        String wordwithoneleter = "";
        for (char i = 'a'; i <= 'z'; i++) {
            wordwithoneleter = sortLetters(word.concat("" + i));
            if (lettersToWord.containsKey(wordwithoneleter)) {
                result.addAll(lettersToWord.get(wordwithoneleter));
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {

        ArrayList<String> words = sizeToWords.get(wordLength);

        if (wordLength < MAX_WORD_LENGTH) {
            wordLength++;
        }

        while (true) {
            String w = words.get(random.nextInt(words.size()));
            if (getAnagramsWithOneMoreLetter(w).size() >= MIN_NUM_ANAGRAMS) {
                return w;
            }
        }

    }

    private String sortLetters(String word) {
        char[] charArray = word.toCharArray();
        Arrays.sort(charArray);
        String sortedString = new String(charArray);
        return sortedString;
    }
}
