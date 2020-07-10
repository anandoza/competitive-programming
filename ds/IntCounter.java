package library.ds;

import library.util.Util;

public class IntCounter {
    int[] freq;
    int distinct = 0;

    /**
     * Tracks frequency for ints in [0, n)
     */
    public IntCounter(int n) {
        freq = new int[n];
    }

    /**
     * Creates frequency table for {@code array}, supporting elements in [0, max(array)].
     */
    public IntCounter(int[] array) {
        int n = Util.max(array) + 1;
        freq = new int[n];
        for (int x : array) {
            incr(x);
        }
    }

    public void incr(int key) {
        if (freq[key] == 0)
            distinct++;
        freq[key]++;
    }

    public void decr(int key) {
        freq[key]--;
        if (freq[key] == 0)
            distinct--;
    }

    public void add(int key, int add) {
        if (freq[key] == 0 && add > 0)
            distinct++;
        freq[key] += add;
        if (freq[key] == 0 && add < 0)
            distinct--;
    }

    public int get(int key) {
        return freq[key];
    }

    public boolean contains(int key) {
        return freq[key] > 0;
    }

    public int distinct() {
        return distinct;
    }
}
