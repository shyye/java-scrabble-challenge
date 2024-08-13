package com.booleanuk;

import java.util.Arrays;
import java.util.List;

public class Scrabble {
    private int score = 0;
    private String word;
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
    }

    public int score() {

        // Add points to score
        for (char character : this.word.toCharArray()) {
            if(this.onePoints.contains(character)) {
                this.score += 1;
            } else if (this.twoPoints.contains(character)) {
                this.score += 2;
            } else if (this.threePoints.contains(character)) {
                this.score += 3;
            } else if (this.fourPoints.contains(character)) {
                this.score += 4;
            } else if (this.fivePoints.contains(character)) {
                this.score += 5;
            } else if (eightPoints.contains(character)) {
                this.score += 8;
            } else if (tenPoints.contains(character)) {
                this.score += 10;
            }
        }
        return this.score;
    }
}
