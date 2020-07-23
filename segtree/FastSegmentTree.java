package library.segtree;

import library.segtree.FastSegmentTree.Storage;

import java.util.function.IntFunction;

public class FastSegmentTree<T extends Storage<T>> {

    public final int size;
    public final T value;

    public interface Storage<T extends Storage<T>> {
        void clear(int i);

        void set(int to, int from);

        void combine(int target, int left, int right);
    }

    public FastSegmentTree(int size, IntFunction<T> constructor) {
        this.size = size;
        value = constructor.apply(2 * size + 4);
        for (int i = 0; i < 2 * size + 2; i++) {
            value.clear(i);
        }
    }

    public void rebuild() {
        for (int i = size - 1; i > 0; i--) {
            value.combine(i, 2 * i, 2 * i + 1);
        }
    }

    /**
     * Copies from index i to special index 0.
     */
    public void get(int i) {
        value.set(0, size + i);
    }

    /**
     * 0 <= i < size
     * <p>
     * Copies from special index 0 to index i.
     */
    public void update(int i) {
        i += size;
        value.set(i, 0);
        while (i > 1) {
            i /= 2;
            value.combine(i, 2 * i, 2 * i + 1);
        }
    }

    /**
     * 0 <= i < size
     * <p>
     * Copies from special index 0 to index i.
     */
    public void update_LAZY(int i) {
        i += size;
        value.set(i, 0);
    }

    /**
     * Returns the sum of the values at indices [i, j) in O(logS)
     * <p>
     * Stores it in special index 0.
     */
    public void query(int i, int j) {
        int left = 2 * size, right = 2 * size + 1;
        int nextLeft = 2 * size + 2, nextRight = 2 * size + 3;
        value.clear(left);
        value.clear(right);
        value.clear(nextLeft);
        value.clear(nextRight);
        for (i += size, j += size; i < j; i /= 2, j /= 2) {
            if ((i & 1) == 1) {
                value.combine(nextLeft, left, i++);
                int t = left;
                left = nextLeft;
                nextLeft = t;
            }
            if ((j & 1) == 1) {
                value.combine(nextRight, --j, right);
                int t = right;
                right = nextRight;
                nextRight = t;
            }
        }
        value.combine(0, left, right);
    }
}