package library.segtree;

import java.util.Arrays;
import java.util.function.IntPredicate;

public class FullIntSegmentTree extends IntSegmentTree {

    public FullIntSegmentTree(int size, IntSegmentTree.Combiner combiner, int identityElement) {
        super(1 << (32 - Integer.numberOfLeadingZeros(size)), combiner, identityElement);
    }

    /*
     * Returns the first i >= 0 such that bigEnough(query(0,i))
     * evaluates to true. Returns -1 if no such i exists.
     * Requires that bigEnough(query(0,i)) is non-decreasing in i.
     */
    public int lowerBound(IntPredicate bigEnough) {
        if (!bigEnough.test(value[1]))
            return -1;

        if (bigEnough.test(identityElement))
            return 0;

        int loc = 1;
        int cur = identityElement;
        for (; loc < size; ) {
            int mid = combine(cur, value[2 * loc]);
            if (bigEnough.test(mid)) {
                loc = 2 * loc;
            } else {
                cur = mid;
                loc = 2 * loc + 1;
            }
        }

        return loc - size + 1;
    }

    public static void main(String[] args) {
        FullIntSegmentTree tree = new FullIntSegmentTree(10, Integer::max, 0);
        for (int i = 0; i < 10; i++) {
            tree.update(i, 10 * (i + 1));
            //            tree.update(i, 1);
        }

        System.out.println(Arrays.toString(tree.value));

        for (int i = 0; i <= 10; i++) {
            System.out.format("%5d:", i);
            for (int j = 0; j <= 10; j++) {
                System.out.format("%5d", tree.query(i, j));
            }
            System.out.println();
        }

        for (int threshold = 5; threshold <= 105; threshold += 5) {
            int finalThreshold = threshold;
            IntPredicate bigEnough = max -> max >= finalThreshold;
            System.out.format("%5d: %5d%n", threshold, tree.lowerBound(bigEnough));
        }
    }
}