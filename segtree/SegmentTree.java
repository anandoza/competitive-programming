package library.segtree;

import java.util.Arrays;

public class SegmentTree<T> {

    public int size;
    public T[] value;

    private final Combiner<T> combiner;
    private final T identityElement;

    public interface Combiner<T> {
        T combine(T a, T b);
    }

    public SegmentTree(int size, Combiner<T> combiner, T identityElement) {
        this.size = size;
        value = (T[]) new Object[2 * size];
        Arrays.fill(value, identityElement);
        this.combiner = combiner;
        this.identityElement = identityElement;
    }

    private T combine(T a, T b) {
        return combiner.combine(a, b);
    }

    public void rebuild() {
        for (int i = size - 1; i > 0; i--) {
            value[i] = combine(value[2 * i], value[2 * i + 1]);
        }
    }

    public T get(int i) {
        return value[size + i];
    }

    /**
     * 0 <= i < size
     */
    public void update(int i, T v) {
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
    public void update_LAZY(int i, T v) {
        i += size;
        value[i] = v;
    }

    // Returns the sum of the values at indices [i, j) in O(logS)
    public T query(int i, int j) {
        T res_left = identityElement, res_right = identityElement;
        for (i += size, j += size; i < j; i /= 2, j /= 2) {
            if ((i & 1) == 1)
                res_left = combine(res_left, value[i++]);
            if ((j & 1) == 1)
                res_right = combine(value[--j], res_right);
        }
        return combine(res_left, res_right);
    }

    // Returns the combination of all values except i.
    public T queryExcept(int i) {
        if (i == 0)
            return query(i + 1, size);
        if (i == size - 1)
            return query(0, i);
        return combine(query(0, i), query(i + 1, size));
    }
}