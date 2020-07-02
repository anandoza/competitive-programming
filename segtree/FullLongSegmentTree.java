package library.segtree;

import java.util.function.Function;

public class FullLongSegmentTree extends LongSegmentTree {

    public FullLongSegmentTree(int size, Combiner combiner, int identityElement) {
        super(1 << (32 - Integer.numberOfLeadingZeros(size)), combiner, identityElement);
    }

    /*
     * Returns the first i >= 0 such that bigEnough(query(0,i))
     * evaluates to true. Returns -1 if no such i exists.
     * Requires that bigEnough(query(0,i)) is non-decreasing in i.
     */
    public int lowerBound(Function<Long, Boolean> bigEnough) {
        if (!bigEnough.apply(value[1]))
            return -1;

        if (bigEnough.apply(identityElement))
            return 0;

        int loc = 1;
        long cur = identityElement;
        for (; loc < size; ) {
            long mid = combine(cur, value[2 * loc]);
            if (bigEnough.apply(mid)) {
                loc = 2 * loc;
            } else {
                cur = mid;
                loc = 2 * loc + 1;
            }
        }

        return loc - size + 1;
    }
}