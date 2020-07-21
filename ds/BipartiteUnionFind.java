package library.ds;

import library.util.Util;

import java.util.Objects;

public class BipartiteUnionFind {
    private final int[] __rep;
    private final int[] __size;
    private final boolean[] consistent;
    private final int[] countSameAsRoot;
    private final boolean[] differentFromRoot;

    /**
     * Create a new bipartite union-find with n vertices and no edges.
     */
    public BipartiteUnionFind(int n) {
        __rep = new int[n];
        __size = new int[n];
        consistent = new boolean[n];
        countSameAsRoot = new int[n];
        differentFromRoot = new boolean[n];
        for (int i = 0; i < n; i++) {
            __rep[i] = i;
            __size[i] = 1;
            consistent[i] = true;
            countSameAsRoot[i] = 1;
            differentFromRoot[i] = false;
        }
    }

    /**
     * Return the number of vertices.
     */
    public int length() {
        return __rep.length;
    }

    /**
     * Get the representative (root) for x, and ensure x's relative parity is set.
     */
    public int rep(int x) {
        if (__rep[x] == x) {
            return x;
        }

        int r = rep(__rep[x]);
        differentFromRoot[x] ^= differentFromRoot[__rep[x]];
        __rep[x] = r;
        return r;
    }

    /**
     * Returns the size of x's component.
     */
    public int size(int x) {
        return __size[rep(x)];
    }

    /**
     * Returns whether x is the same parity as its root.
     */
    public boolean sameAsRoot(int x) {
        rep(x);
        return !differentFromRoot[x];
    }

    /**
     * Returns the number of vertices in x's component with the same parity as the root.
     */
    public int countSameAsRoot(int x) {
        return countSameAsRoot[rep(x)];
    }

    /**
     * Return true if x and y have different parity. Throw an exception if they are not in the same component.
     */
    public boolean different(int x, int y) {
        Util.ASSERT(rep(x) == rep(y));
        return differentFromRoot[x] ^ differentFromRoot[y];
    }

    /**
     * Return whether x's component is actually still consistent/bipartite.
     */
    public boolean consistent(int x) {
        return consistent[x];
    }

    /**
     * Connect x and y (and saying they have different parity).
     */
    public Status union(int x, int y) {
        return union(x, y, true);
    }

    /**
     * Connect x and y, passing a variable for whether they have different parity.
     */
    public Status union(int x, int y, boolean different) {
        int xRoot = rep(x);
        int yRoot = rep(y);

        if (xRoot == yRoot) {
            boolean good = !(different ^ differentFromRoot[x] ^ differentFromRoot[y]);
            if (!good)
                consistent[xRoot] = false;
            return good ? Status.FT : Status.FF;
        }

        boolean areRootsDifferent = different ^ differentFromRoot[x] ^ differentFromRoot[y];

        x = xRoot;
        y = yRoot;
        if (size(x) < size(y)) {
            int t = x;
            x = y;
            y = t;
        }

        __rep[y] = x;
        __size[x] += __size[y];
        consistent[x] &= consistent[y];
        differentFromRoot[y] = areRootsDifferent;
        if (areRootsDifferent)
            countSameAsRoot[x] += __size[y] - countSameAsRoot[y];
        else
            countSameAsRoot[x] += countSameAsRoot[y];

        return Status.TT;
    }

    public static class Status {
        public static final Status TT = new Status(true, true);
        public static final Status FT = new Status(false, true);
        public static final Status FF = new Status(false, false);

        public final boolean unionSucceeded, consistent;

        private Status(boolean unionSucceeded, boolean consistent) {
            this.unionSucceeded = unionSucceeded;
            this.consistent = consistent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Status status = (Status) o;
            return unionSucceeded == status.unionSucceeded && consistent == status.consistent;
        }

        @Override
        public int hashCode() {
            return Objects.hash(unionSucceeded, consistent);
        }
    }
}