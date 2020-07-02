package library.sparsetable;

import static library.util.Util.ASSERT;

public class IntSparseTable {

    public int size;
    public int[] table;
    private int maxLength;

    private final Combiner combiner;

    public interface Combiner {
        /**
         * Requires combine(x, x) = x
         */
        int combine(int a, int b);
    }

    public IntSparseTable(int[] array, Combiner combiner) {
        this.size = array.length;
        this.combiner = combiner;

        maxLength = 1 + Integer.numberOfTrailingZeros(Integer.highestOneBit(Math.max(size - 1, 1)));
        table = new int[maxLength * size];
        System.arraycopy(array, 0, table, 0, size);

        rebuild();
    }

    public IntSparseTable(int size, Combiner combiner) {
        this.size = size;
        this.combiner = combiner;

        maxLength = 1 + Integer.numberOfTrailingZeros(Integer.highestOneBit(Math.max(size - 1, 1)));
        table = new int[maxLength * size];
    }

    public void rebuild() {
        for (int l = 0; l + 1 < maxLength; l++) {
            for (int i = 0; i < size; i++) {
                table[index(l + 1, i)] = table[index(l, i)];
                if (i + (1 << l) < size) {
                    table[index(l + 1, i)] = combine(table[index(l + 1, i)], table[index(l, i + (1 << l))]);
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

    private int combine(int a, int b) {
        return combiner.combine(a, b);
    }

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
        return combine(left, right);
    }

    // Returns the combination of the values at indices [i, j) in O(1)
    public int queryOrDefault(int i, int j, int defaultValue) {
        if (!(0 <= i && i < j && j <= size))
            return defaultValue;
        int length = j - i - 1;
        int l = length == 0 ? 0 : Integer.numberOfTrailingZeros(Integer.highestOneBit(length));
        int left = table[index(l, i)];
        int right = table[index(l, j - (1 << l))];
        return combine(left, right);
    }

    // Returns the combination of all values except i.
    public int queryExcept(int i) {
        if (i == 0)
            return query(i + 1, size);
        if (i == size - 1)
            return query(0, i);
        return combine(query(0, i), query(i + 1, size));
    }
}