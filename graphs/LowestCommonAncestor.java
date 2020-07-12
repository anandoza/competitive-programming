package library.graphs;

import library.sparsetable.IntSparseTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LowestCommonAncestor {
    public static class Visit {
        public final int node, depth;

        public Visit(int node, int depth) {
            this.node = node;
            this.depth = depth;
        }

        public Visit add(Visit o) {
            return depth <= o.depth ? this : o;
        }

        @Override
        public String toString() {
            return "Visit{" + "node=" + node + ", depth=" + depth + '}';
        }
    }

    public final int[] depth;
    public final int[] pos;
    public final Visit[] tour;
    IntSparseTable table;

    public LowestCommonAncestor(List<Integer>[] adj, int root) {
        int n = adj.length;
        depth = new int[n];
        pos = new int[n];
        Arrays.fill(pos, -1);

        ArrayList<Visit> tour = new ArrayList<>();

        dfs(root, tour, -1, adj);

        this.tour = new Visit[tour.size()];
        for (int i = 0; i < this.tour.length; i++) {
            this.tour[i] = tour.get(i);
        }

        table = new IntSparseTable(this.tour.length, (a, b) -> this.tour[a].depth < this.tour[b].depth ? a : b);
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
        return tour[table.query(u, v)].node;
    }

    void dfs(int root, ArrayList<Visit> tour, int parent, List<Integer>[] adj) {
        pos[root] = tour.size();
        tour.add(new Visit(root, depth[root]));
        for (int next : adj[root]) {
            if (next == parent)
                continue;
            depth[next] = depth[root] + 1;
            dfs(next, tour, root, adj);
            tour.add(new Visit(root, depth[root]));
        }
    }
}
