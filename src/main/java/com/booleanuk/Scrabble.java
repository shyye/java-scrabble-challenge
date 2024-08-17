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

    public boolean extractStringWithRegexBoolean(String word, String regexPattern) {

        // Reference: https://docs.oracle.com/javase/tutorial/essential/regex/test_harness.html
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(word);
        return matcher.find();
    }

    public int score() {

        // Regex patterns
        // Regex tips https://regexr.com/
        String regexDouble = "\\{.*?\\}";
        String regexTriple = "\\[.*?\\]";
        String regexNotValidDouble = "\\}.*?\\{|\\{.*?\\]";
        String regexNotValidTriple = "\\].*?\\[|\\[.*?\\}";
//        String regexTriple = "\\[[^\\[].*?\\]";
        String regexLetters = "[a-zA-Z]+";
        String regexWordsWithSpecialCharacters = "[^a-zA-Z]+";  // Can include letters, special charaacters, brackets
//        String regexOnlyLetters = "(?=[a-zA-Z]+)(?=[^a-zA-Z]+)";    // WRONG
        String regexNotLetters = "[^a-zA-Z]+";
        String regexNotLettersButBrackets = "[^a-zA-Z\\{\\}\\[\\]]+";
        String regexInvalidBracketsLeftSquare = "\\[{1}";     // Check if only appear once
        String regexInvalidBracketsLeftCurly = "\\{{1}";
        String regexInvalidBracketsRightSquare = "\\]{1}";
        String regexInvalidBracketsRightCurly = "\\}{ 1}";
        // TODO check if can replace with
        String regexInvalidSingleBracket = "(\\{|\\})(\\[|\\]){1}";
        String regexDuplicationOfLetters = "(\\w)\\1+";         // A sequence of the same letter e.g. "ll" in he{ll}o

        boolean isDouble = extractStringWithRegexBoolean(this.word, regexDouble);
        boolean isTriple = extractStringWithRegexBoolean(this.word, regexTriple);
        boolean isOnlyLetters = !extractStringWithRegexBoolean(this.word, regexWordsWithSpecialCharacters);     // Is only letters if it is NOT a word with mixed letters, special characters and brackets

        if (isDouble || isTriple || isOnlyLetters) {

            if (isDouble) {
                // Extract double letters, brackets included
                String doubleLetters = extractStringWithRegex(this.word, regexDouble);

                // Check for invalid duplication of letters inside brakcets, e.g. he{ll}o
                boolean invalidDuplication = extractStringWithRegexBoolean(doubleLetters, regexDuplicationOfLetters);
                if (invalidDuplication) {
                    return 0;
                }

                // Extract only letters, escape brackets
                doubleLetters = extractStringWithRegex(doubleLetters, regexLetters);
                // Calculate double points
                for (char letter : doubleLetters.toCharArray()) {
                    this.score += pointsMap.get(letter) * 2;
                }
            }

            if (isTriple) {
                // Extract triple letters, brackets included
                String tripleLetters = extractStringWithRegex(this.word, regexTriple);

                // Check for invalid duplication of letters inside brakcets, e.g. he{ll}o
                boolean invalidDuplication = extractStringWithRegexBoolean(tripleLetters, regexDuplicationOfLetters);
                if (invalidDuplication) {
                    return 0;
                }

                // Extract only letters, escape brackets
                tripleLetters = extractStringWithRegex(tripleLetters, regexLetters);
                // Calculate triple points
                for (char letter : tripleLetters.toCharArray()) {
                    this.score += pointsMap.get(letter) * 3;
                }
            }

            // Calculate ordinary/base points
            String baseLetters = this.word.replaceAll(regexDouble, "");     // Remove double points
            baseLetters = baseLetters.replaceAll(regexTriple, "");           // Remove triple points
            for (char letter : baseLetters.toCharArray()) {
                this.score += pointsMap.get(letter);
            }

            return this.score;
        }
        return 0;


        // Check invalid brackets
//        boolean invalidDouble = extractStringWithRegexBoolean(this.word, regexNotValidDouble);
//        boolean invalidTripel = extractStringWithRegexBoolean(this.word, regexNotValidTriple);
//        if (invalidDouble || invalidTripel) {
//            return this.score;
//        }

        // Check if there is only one occurence of { [ ] or }, with the help of an XOR gate (exclusive or)
//        boolean invalidBracketLeftSquare = extractStringWithRegexBoolean(this.word, regexInvalidBracketsLeftSquare);
//        boolean invalidBracketLeftCurly = extractStringWithRegexBoolean(this.word, regexInvalidBracketsLeftCurly);
//        boolean invalidBracketRightSquare = extractStringWithRegexBoolean(this.word, regexInvalidBracketsRightSquare);
//        boolean invalidBracketRightCurly = extractStringWithRegexBoolean(this.word, regexInvalidBracketsRightCurly);
//        if (invalidBracketLeftSquare ^ invalidBracketRightSquare) {
//            return this.score;
//        }
//        if (invalidBracketLeftCurly ^ invalidBracketRightCurly) {
//            return this.score;
//        }

        // Check invalid when there is only one single bracket
//        boolean invalidSingleBracket = extractStringWithRegexBoolean(this.word, regexInvalidSingleBracket);
//        if (invalidSingleBracket) {
//            return this.score;
//        }
//
//        // Check if word has invalid tokens
//        boolean invalidTokens = extractStringWithRegexBoolean(this.word, regexNotLettersButBrackets);
//        if (invalidTokens) {
//            return 0;
//        }
//
//        // Invalid double letters in brackets e.g. "he{ll}o
//        boolean invalidDoubleLettersInsideBrackets;
//
//        // Double points
//        String doubleLetters = extractStringWithRegex(this.word, regexDouble);
//        // Check if invalid
//        invalidDoubleLettersInsideBrackets = extractStringWithRegexBoolean(doubleLetters, regexSameLetterSequence);
//        if (invalidDoubleLettersInsideBrackets) {
//            return this.score;
//        }
//        // Calculate double points
//        doubleLetters = extractStringWithRegex(doubleLetters, regexOnlyLetters);    // Extract only letters, escape brackets
//        for (char letter : doubleLetters.toCharArray()) {
//            this.score += pointsMap.get(letter) * 2;
//        }
//
//        // Calculate triple points
//        String tripleLetters = extractStringWithRegex(this.word, regexTriple);
//        tripleLetters = extractStringWithRegex(tripleLetters, regexOnlyLetters);    // Extract only letters, escape brackets
//        for (char letter : tripleLetters.toCharArray()) {
//            this.score += pointsMap.get(letter) * 3;
//        }
//
//        // Calcualte ordinary/base points
//        String baseLetters = this.word.replaceAll(regexDouble, "");     // Remove double points
//        baseLetters = baseLetters.replaceAll(regexTriple, "");           // Remove triple points
//        for (char letter : baseLetters.toCharArray()) {
//            this.score += pointsMap.get(letter);
//        }

//        return this.score;
    }
}
