package library.ds;

public class UnionFind {

    private int[] __rep;
    private int[] __size;

    public UnionFind(int n) {
        __rep = new int[n];
        __size = new int[n];
        for (int i = 0; i < n; i++) {
            __rep[i] = i;
            __size[i] = 1;
        }
    }

    public int rep(int x) {
        if (__rep[x] == x) {
            return x;
        }

        int r = rep(__rep[x]);
        __rep[x] = r;
        return r;
    }

    public int size(int x) {
        return __size[rep(x)];
    }

    public boolean union(int x, int y) {
        x = rep(x);
        y = rep(y);

        if (x == y) {
            return false;
        }

        if (size(x) < size(y)) {
            int t = x;
            x = y;
            y = t;
        }

        // now size(x) >= size(y)

        __rep[y] = x;
        __size[x] += __size[y];
        return true;
    }

}