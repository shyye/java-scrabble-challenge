package com.booleanuk;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scrabble {
    private int score = 0;
    private String word;

    private HashMap<Character, Integer> pointsMap = new HashMap<>();
    // TODO: Check meaning of final
    private final List<Character> onePoints = Arrays.asList('A', 'E', 'I', 'O', 'U', 'L', 'N', 'R', 'S', 'T');
    private final List<Character> twoPoints = Arrays.asList('D', 'G');
    private final List<Character> threePoints = Arrays.asList('B', 'C', 'M', 'P');
    private final List<Character> fourPoints = Arrays.asList('F', 'H', 'V', 'W', 'Y');
    private final List<Character> fivePoints = Arrays.asList('K');
    private final List<Character> eightPoints = Arrays.asList('J', 'X');
    private final List<Character> tenPoints = Arrays.asList('Q', 'Z');

    public Scrabble(String word) {
        this.word = word.toUpperCase();

        // TODO: Is there a better way?
        // Initialize pointsMap for the letters and the corresponding points
        onePoints.forEach( letter -> pointsMap.put(letter, 1));
        twoPoints.forEach( letter -> pointsMap.put(letter, 2));
        threePoints.forEach( letter -> pointsMap.put(letter, 3));
        fourPoints.forEach( letter -> pointsMap.put(letter, 4));
        fivePoints.forEach( letter -> pointsMap.put(letter, 5));
        eightPoints.forEach( letter -> pointsMap.put(letter, 8));
        tenPoints.forEach( letter -> pointsMap.put(letter, 10));
    }

    public ArrayList<Integer> extractLettersWithRegex(String word, String regexPattern) {
        // TODO: Should I change to ordinary list to save memory? With just two elements.
        ArrayList<Integer> output = new ArrayList<>();

        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(word);

        boolean found = false;
        while (matcher.find()) {
            System.out.printf("I found the text" +
                            " \"%s\" starting at " +
                            "index %d and ending at index %d.%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());
            found = true;
            //output += matcher.group();
            output.add(matcher.start());
            output.add(matcher.end());
        }
        if(!found){
            System.out.printf("No match found.%n");
        }
        System.out.println("text: " + output);
        return output;
    }

    public String extractStringWithRegex(String word, String regexPattern) {

        // Reference: https://docs.oracle.com/javase/tutorial/essential/regex/test_harness.html
        String output = "";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(word);

        boolean found = false;
        while (matcher.find()) {
            System.out.printf("I found the text" +
                            " \"%s\" starting at " +
                            "index %d and ending at index %d.%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());
            found = true;
            output += matcher.group();
        }
        if(!found){
            System.out.printf("No match found.%n");
        }
        System.out.println("text: " + output);
        return output;
    }

    public int score() {

        // Regex patterns
        String regexDouble = "\\{.*?\\}";
        String regexTriple = "\\[.*?\\]";
        String regexOnlyLetters = "[a-zA-Z]+";
        String regexNotLetters = "[^a-zA-Z]+";
        String regexNotLettersButBrackets = "[^a-zA-Z\\{\\}\\[\\]]+";

        // Check if word has invalid tokens
        Pattern pattern = Pattern.compile(regexNotLettersButBrackets);
        Matcher matcher = pattern.matcher(this.word);
        boolean invalidTokens = matcher.find();
        if (invalidTokens) {
            return this.score;
        }

        // Calculate double points
        String doubleLetters = extractStringWithRegex(this.word, regexDouble);
        doubleLetters = extractStringWithRegex(doubleLetters, regexOnlyLetters);    // Extract only letters, escape brackets
        for (char letter : doubleLetters.toCharArray()) {
            this.score += pointsMap.get(letter) * 2;
        }

        // Calculate triple points
        String tripleLetters = extractStringWithRegex(this.word, regexTriple);
        tripleLetters = extractStringWithRegex(tripleLetters, regexOnlyLetters);    // Extract only letters, escape brackets
        for (char letter : tripleLetters.toCharArray()) {
            this.score += pointsMap.get(letter) * 3;
        }

        // Calcualte ordinary/base points
        String baseLetters = this.word.replaceAll(regexDouble, "");     // Remove double points
        baseLetters = baseLetters.replaceAll(regexTriple, "");           // Remove triple points
        for (char letter : baseLetters.toCharArray()) {
            this.score += pointsMap.get(letter);
        }

        return this.score;
    }
}
