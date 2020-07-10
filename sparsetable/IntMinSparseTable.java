package library.sparsetable;

import static library.util.Util.ASSERT;

public class IntMinSparseTable {

    public final int size;
    public final int[] table;
    private final int maxLength;

    public IntMinSparseTable(int[] array) {
        this.size = array.length;

        maxLength = 1 + Integer.numberOfTrailingZeros(Integer.highestOneBit(Math.max(size - 1, 1)));
        table = new int[maxLength * size];
        System.arraycopy(array, 0, table, 0, size);

        rebuild();
    }

    public IntMinSparseTable(int size) {
        this.size = size;

        maxLength = 1 + Integer.numberOfTrailingZeros(Integer.highestOneBit(Math.max(size - 1, 1)));
        table = new int[maxLength * size];
    }

    public void rebuild() {
        for (int l = 0; l + 1 < maxLength; l++) {
            for (int i = 0; i < size; i++) {
                table[index(l + 1, i)] = table[index(l, i)];
                if (i + (1 << l) < size) {
                    table[index(l + 1, i)] = Math.min(table[index(l + 1, i)], table[index(l, i + (1 << l))]);
                }
            }
        }
    }

    /**
     * 0 <= i < size. Must rebuild after!
     */
    public void update_LAZY(int i, int v) {
        table[i] = v;
    }

    private int index(int length, int start) {
        return length * size + start;
    }

    /**
     * 0 <= i < size.
     */
    public int get(int i) {
        return table[i];
    }

    // Returns the combination of the values at indices [i, j) in O(1)
    public int query(int i, int j) {
        ASSERT(0 <= i && i < j && j <= size);
        int length = j - i - 1;
        int l = length == 0 ? 0 : Integer.numberOfTrailingZeros(Integer.highestOneBit(length));
        int left = table[index(l, i)];
        int right = table[index(l, j - (1 << l))];
        return Math.min(left, right);
    }
}