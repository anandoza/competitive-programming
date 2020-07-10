package library.sparsetable;

import java.util.Arrays;

import static library.util.Util.ASSERT;

public class SparseTable<T> {

    public final int size;
    public final T[] table;

    private final Combiner<T> combiner;

    public interface Combiner<T> {
        /**
         * Requires combine(x, x) = x
         */
        T combine(T a, T b);
    }

    public SparseTable(T[] array, Combiner<T> combiner) {
        this.size = array.length;
        this.combiner = combiner;

        int maxLength = 1 + Integer.numberOfTrailingZeros(Integer.highestOneBit(Math.max(size - 1, 1)));
        table = (T[]) new Object[maxLength * size];
        System.arraycopy(array, 0, table, 0, size);

        for (int l = 0; l + 1 < maxLength; l++) {
            for (int i = 0; i < size; i++) {
                table[index(l + 1, i)] = table[index(l, i)];
                if (i + (1 << l) < size) {
                    table[index(l + 1, i)] = combine(table[index(l + 1, i)], table[index(l, i + (1 << l))]);
                }
            }
        }
    }

    private int index(int length, int start) {
        return length * size + start;
    }

    private T combine(T a, T b) {
        return combiner.combine(a, b);
    }

    public T get(int i) {
        return table[i];
    }

    // Returns the combination of the values at indices [i, j) in O(1)
    public T query(int i, int j) {
        ASSERT(0 <= i && i < j && j <= size);
        int length = j - i - 1;
        int l = length == 0 ? 0 : Integer.numberOfTrailingZeros(Integer.highestOneBit(length));
        T left = table[index(l, i)];
        T right = table[index(l, j - (1 << l))];
        return combine(left, right);
    }

    // Returns the combination of the values at indices [i, j) in O(1)
    public T queryOrDefault(int i, int j, T defaultValue) {
        if (!(0 <= i && i < j && j <= size))
            return defaultValue;
        int length = j - i - 1;
        int l = length == 0 ? 0 : Integer.numberOfTrailingZeros(Integer.highestOneBit(length));
        T left = table[index(l, i)];
        T right = table[index(l, j - (1 << l))];
        return combine(left, right);
    }

    // Returns the combination of all values except i.
    public T queryExcept(int i) {
        if (i == 0)
            return query(i + 1, size);
        if (i == size - 1)
            return query(0, i);
        return combine(query(0, i), query(i + 1, size));
    }

    public static void main(String[] args) {
        Integer[] a = {3, 1, 4, 1, 5, 9, 2, 6};
        SparseTable<Integer> table = new SparseTable<>(a, Math::min);

//        int[][] min = new int[a.length + 1][a.length + 1];
//        for (int i = 0; i < a.length; i++) {
//            for (int j = i + 1; j <= a.length; j++) {
//                min[i][j] = table.query(i, j);
//            }
//        }
        System.out.println(table.query(0, 3));

//        Util.print2DArray(min);
        System.out.println(Arrays.toString(table.table));
    }
}