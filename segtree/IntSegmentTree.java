package library.segtree;

import java.util.Arrays;

public class IntSegmentTree {

    public final int size;
    public final int[] value;

    protected final Combiner combiner;
    protected final int identityElement;

    public interface Combiner {
        int combine(int a, int b);
    }

    public IntSegmentTree(int size, Combiner combiner, int identityElement) {
        this.size = size;
        value = new int[2 * size];
        Arrays.fill(value, identityElement);
        this.combiner = combiner;
        this.identityElement = identityElement;
    }

    protected int combine(int a, int b) {
        return combiner.combine(a, b);
    }

    public void rebuild() {
        for (int i = size - 1; i > 0; i--) {
            value[i] = combine(value[2 * i], value[2 * i + 1]);
        }
    }

    public int get(int i) {
        return value[size + i];
    }

    /**
     * 0 <= i < size
     */
    public void update(int i, int v) {
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
    public void update_LAZY(int i, int v) {
        i += size;
        value[i] = v;
    }

    /**
     * Returns the sum of the values at indices [i, j) in O(logS)
     * <p>
     * 0 <= i,j <= size
     */
    public int query(int i, int j) {
        int res_left = identityElement, res_right = identityElement;
        for (i += size, j += size; i < j; i /= 2, j /= 2) {
            if ((i & 1) == 1)
                res_left = combine(res_left, value[i++]);
            if ((j & 1) == 1)
                res_right = combine(value[--j], res_right);
        }
        return combine(res_left, res_right);
    }

}