package library.segtree;

public class IntSumSegmentTree {

    public int size;
    public int[] value;

    public IntSumSegmentTree(int size) {
        this.size = size;
        value = new int[2 * size];
    }

    public void rebuild() {
        for (int i = size - 1; i > 0; i--) {
            value[i] = value[2 * i] + value[2 * i + 1];
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
            value[i] = value[2 * i] + value[2 * i + 1];
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
        int res_left = 0, res_right = 0;
        for (i += size, j += size; i < j; i /= 2, j /= 2) {
            if ((i & 1) == 1) {
                int b = value[i++];
                res_left = res_left + b;
            }
            if ((j & 1) == 1) {
                int a = value[--j];
                res_right = a + res_right;
            }
        }
        return res_left + res_right;
    }

}