package library.segtree;

import java.util.Arrays;

public class LazyIntAdditiveSegmentTree {

    public final int size;
    public final int[] value;
    public final boolean[] has;
    public final int[] ops;

    protected final Combiner combiner;
    protected final int identityElement;
    protected final int updaterIdentityElement = 0;

    public interface Combiner {
        int combine(int a, int b);
    }

    public LazyIntAdditiveSegmentTree(int size, Combiner combiner, int identityElement) {
        this.size = size;
        value = new int[2 * size];
        Arrays.fill(value, identityElement);
        has = new boolean[size];
        ops = new int[size];
        Arrays.fill(ops, updaterIdentityElement);
        this.combiner = combiner;
        this.identityElement = identityElement;
    }

    void _apply(int i, int u) {
        value[i] = add(u, value[i]);
        if (i < size) {
            has[i] = true;
            ops[i] += u;
        }
    }

    void _rebuild(int i) {
        for (i /= 2; i > 0; i /= 2) {
            value[i] = add(ops[i], combine(value[2 * i], value[2 * i + 1]));
        }
    }

    void _propagate(int i) {
        for (int j = 31 - Integer.numberOfLeadingZeros(i); j > 0; j--) {
            int k = i >> j;
            if (has[k]) {
                _apply(2 * k, ops[k]);
                _apply(2 * k + 1, ops[k]);
                has[k] = false;
                ops[k] = updaterIdentityElement;
            }
        }
    }

    private static int add(int update, int value) {
        if (value == Integer.MAX_VALUE || value == Integer.MIN_VALUE)
            return value;
        return value + update;
    }

    protected int combine(int a, int b) {
        return combiner.combine(a, b);
    }

    public int get(int i) {
        return query(i, i + 1);
    }

    /**
     * 0 <= i < size
     */
    public void replace(int i, int v) {
        i += size;
        _propagate(i);
        value[i] = v;
        _rebuild(i);
    }

    public void apply(int i, int j, int u) {
        i += size;
        j += size;
        _propagate(i);
        _propagate(j - 1);
        for (int l = i, r = j; l < r; l /= 2, r /= 2) {
            if ((l & 1) > 0)
                _apply(l++, u);
            if ((r & 1) > 0)
                _apply(--r, u);
        }
        _rebuild(i);
        _rebuild(j - 1);
    }

    public int query(int i, int j) {
        i += size;
        j += size;
        _propagate(i);
        _propagate(j - 1);
        int res_left = identityElement, res_right = identityElement;
        for (; i < j; i /= 2, j /= 2) {
            if ((i & 1) == 1)
                res_left = combine(res_left, value[i++]);
            if ((j & 1) == 1)
                res_right = combine(value[--j], res_right);
        }
        return combine(res_left, res_right);
    }

    public static void main(String[] args) {
        LazyIntAdditiveSegmentTree max = new LazyIntAdditiveSegmentTree(5, Integer::max, -1);
        max.apply(3, 5, 5);
        System.out.println(max.query(3, 5));

    }
}