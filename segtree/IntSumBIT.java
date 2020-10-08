package library.segtree;

public class IntSumBIT {

    public final int size;
    public final int[] value;

    public IntSumBIT(int size) {
        this.size = size;
        value = new int[size + 1];
    }

    public void update(int i, int v) {
        add(i, v - query(i, i + 1));
    }

    public void add(int i, int v) {
        for (i++; i <= size; i += i & -i)
            value[i] += v;
    }

    public int get(int i) {
        return query(i, i + 1);
    }

    public int sum(int i) {
        int r = 0;
        for (; i > 0; i -= i & -i)
            r = r + value[i];
        return r;
    }

    /**
     * Returns the sum of the values at indices [i, j) in O(logS)
     * <p>
     * 0 <= i,j <= size
     */
    public int query(int i, int j) {
        return i >= j ? 0 : sum(j) - sum(i);
    }
}