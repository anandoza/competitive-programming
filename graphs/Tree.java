package library.graphs;

import library.util.Util;

import java.util.ArrayList;
import java.util.List;

import static library.graphs.Graphs.Edge;

public class Tree {

    public final int[] v;
    public final List<Integer>[] adj;
    public final Edge[] edges;

    public Tree(int[] v, Edge[] edges) {
        this(v.length, edges);
    }

    public Tree(int n, Edge[] edges) {
        Util.ASSERT(edges.length == n - 1);

        v = new int[n];

        this.edges = edges;

        this.adj = new List[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int edgeIndex = 0; edgeIndex < edges.length; edgeIndex++) {
            adj[edges[edgeIndex].i].add(edgeIndex);
            adj[edges[edgeIndex].j].add(edgeIndex);
        }
    }

    public <T> T[] computeForAllSubtrees(NodeEvaluator<T> evaluator) {
        int root = 0;
        T[] dp = (T[]) new Object[2 * edges.length + 1];

        dp[2 * edges.length] = computeForAllSubtreesHelper(root, -1, dp, evaluator);

        return dp;
    }

    private <T> T computeForAllSubtreesHelper(int root, int parent, T[] dp, NodeEvaluator<T> evaluator) {
        ArrayList<T> children = new ArrayList<>();
        for (int e : adj[root]) {
            int child = edges[e].i + edges[e].j - root;
            if (child == parent) {
                children.add(null);
                continue;
            }

            children.add(computeForAllSubtreesHelper(child, root, dp, evaluator));
            dp[e + edges[e].i == root ? 0 : edges.length] = children.get(children.size() - 1);
        }

        T result = evaluator.eval(root, children);
        return result;
    }

    public interface NodeEvaluator<T> {
        T eval(int currentNodeIndex, List<T> childrenValues);
    }
}
