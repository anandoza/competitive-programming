package library.graphs;

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

    public int[] depth, pos;
    public Visit[] tour;

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
