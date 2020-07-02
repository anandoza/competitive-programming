package library.string;

import java.util.Arrays;

public class KMP {
    public final char[] pattern;
    public final int[] fail;

    /**
     * Initializes the failure table.
     */
    public KMP(char[] pattern) {
        this.pattern = pattern;
        fail = new int[pattern.length + 1];

        fail[0] = 0;
        for (int i = 2; i <= pattern.length; i++) {
            fail[i] = adv(fail[i - 1], pattern[i - 1]);
        }
    }

    public int adv(int len, char next) {
        while (len > 0 && pattern[len] != next) {
            len = fail[len];
        }
        return len + (pattern[len] == next ? 1 : 0);
    }

    public static int adv(int len, char next, char[] pattern, int[] fail) {
        while (len > 0 && pattern[len] != next) {
            len = fail[len];
        }
        return len + (pattern[len] == next ? 1 : 0);
    }

    /**
     * Returns a boolean array where match[i] indicates whether a match starts at index i.
     */
    public boolean[] find(char[] text) {
        boolean[] match = new boolean[text.length];
        for (int i = 0, len = 0; i < text.length; i++) {
            len = adv(len, text[i]);
            if (len == pattern.length) {
                match[i - len + 1] = true;
                len = fail[len];
            }
        }
        return match;
    }

    public int[] computeScores(char[] text) {
        int[] score = new int[text.length];
        for (int i = 0, len = 0; i < text.length; i++) {
            len = adv(len, text[i]);
            score[i] = len;
            if (len == pattern.length) {
                len = fail[len];
            }
        }
        return score;
    }

    public static void main(String[] args) {
        KMP k = new KMP("ana".toCharArray());
        System.out.println(Arrays.toString(k.find("banana".toCharArray())));
        System.out.println(Arrays.toString(k.computeScores("banan".toCharArray())));
    }
}
