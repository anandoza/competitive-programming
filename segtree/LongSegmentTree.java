package library.segtree;

import java.util.Arrays;

public class LongSegmentTree {

    public int size;
    public long[] value;

    protected final Combiner combiner;
    protected final long identityElement;

    public interface Combiner {
        long combine(long a, long b);
    }

    public LongSegmentTree(int size, Combiner combiner, long identityElement) {
        this.size = size;
        value = new long[2 * size];
        Arrays.fill(value, identityElement);
        this.combiner = combiner;
        this.identityElement = identityElement;
    }

    protected long combine(long a, long b) {
        return combiner.combine(a, b);
    }

    public void rebuild() {
        for (int i = size - 1; i > 0; i--) {
            value[i] = combine(value[2 * i], value[2 * i + 1]);
        }
    }

    public long get(int i) {
        return value[size + i];
    }

    /**
     * 0 <= i < size
     */
    public void update(int i, long v) {
        i += size;
        value[i] = v;
        while (i > 1) {
            i /= 2;
            value[i] = combine(value[2 * i], value[2 * i + 1]);
        }
    }

    /**
     * 0 <= i < size
     */
    public void update_LAZY(int i, long v) {
        i += size;
        value[i] = v;
    }

    /**
     * Returns the sum of the values at indices [i, j) in O(logS)
     * <p>
     * 0 <= i,j <= size
     */
    public long query(int i, int j) {
        long res_left = identityElement, res_right = identityElement;
        for (i += size, j += size; i < j; i /= 2, j /= 2) {
            if ((i & 1) == 1)
                res_left = combine(res_left, value[i++]);
            if ((j & 1) == 1)
                res_right = combine(value[--j], res_right);
        }
        return combine(res_left, res_right);
    }

}