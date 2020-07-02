package library.graphs;

import library.InputReader;
import library.util.pair.Pair;

import java.util.ArrayList;
import java.util.List;

public class GraphIO {
    public static List<Integer>[] readUndirectedAdjacencyList(InputReader in, int n, int m) {
        List<Integer>[] adj = new List[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            adj[u].add(v);
            adj[v].add(u);
        }
        return adj;
    }

    public static List<Integer>[] readDirectedAdjacencyList(InputReader in, int n, int m) {
        List<Integer>[] adj = new List[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            adj[u].add(v);
        }
        return adj;
    }

    public static Pair<Graphs.Edge[], List<Integer>[]> readWeightedDirectedAdjacencyList(InputReader in, int n, int m) {
        Graphs.Edge[] edges = new Graphs.Edge[m];
        List<Integer>[] adj = new List[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            int a = in.nextInt() - 1;
            int b = in.nextInt() - 1;
            int t = in.nextInt();
            edges[i] = new Graphs.Edge(a, b, t);
            adj[a].add(i);
        }

        return Pair.of(edges, adj);
    }

    public static Pair<Graphs.Edge[], List<Integer>[]> readWeightedAdjacencyList(InputReader in, int n, int m) {
        Graphs.Edge[] edges = new Graphs.Edge[m];
        List<Integer>[] adj = new List[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            int a = in.nextInt() - 1;
            int b = in.nextInt() - 1;
            int t = in.nextInt();
            edges[i] = new Graphs.Edge(a, b, t);
            adj[a].add(i);
            adj[b].add(i);
        }

        return Pair.of(edges, adj);
    }

    private GraphIO() {
    }
}
