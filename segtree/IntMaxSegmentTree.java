package library.segtree;

import java.util.Arrays;

public class IntMaxSegmentTree {

    public int size;
    public int[] value;

    protected final int identityElement;

    public IntMaxSegmentTree(int size) {
        this.size = size;
        value = new int[2 * size];
        this.identityElement = Integer.MIN_VALUE;
        Arrays.fill(value, identityElement);
    }

    public void rebuild() {
        for (int i = size - 1; i > 0; i--) {
            value[i] = Math.max(value[2 * i], value[2 * i + 1]);
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
            value[i] = Math.max(value[2 * i], value[2 * i + 1]);
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
            if ((i & 1) == 1) {
                res_left = Math.max(res_left, value[i++]);
            }
            if ((j & 1) == 1) {
                res_right = Math.max(value[--j], res_right);
            }
        }
        return Math.max(res_left, res_right);
    }

}