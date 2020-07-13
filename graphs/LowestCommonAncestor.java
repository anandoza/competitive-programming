package library.graphs;

import library.sparsetable.IntSparseTable;

import java.util.List;

public class LowestCommonAncestor {
    public final int[] depth;
    public final int[] pos;
    public final int[] tour;
    private int tourLength = 0;
    IntSparseTable table;

    public LowestCommonAncestor(List<Integer>[] adj, int root) {
        int n = adj.length;
        depth = new int[n];
        pos = new int[n];
        tour = new int[n + (n - 1)];

        dfs(root, tour, -1, adj);

        table = new IntSparseTable(this.tour.length, (a, b) -> depth[tour[a]] < depth[tour[b]] ? a : b);
        for (int i = 0; i < table.size; i++) {
            table.update_LAZY(i, i);
        }
        table.rebuild();
    }

    public int lca(int u, int v) {
        u = pos[u];
        v = pos[v];

        if (u > v) {
            int t = u;
            u = v;
            v = t;
        }
        return tour[table.query(u, v)];
    }

    void dfs(int root, int[] tour, int parent, List<Integer>[] adj) {
        pos[root] = tourLength;
        tour[tourLength++] = root;
        for (int next : adj[root]) {
            if (next == parent)
                continue;
            depth[next] = depth[root] + 1;
            dfs(next, tour, root, adj);
            tour[tourLength++] = root;
        }
    }
}
