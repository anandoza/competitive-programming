package library.graphs;

import library.ds.UnionFind;
import library.util.Util;
import library.util.pair.Pair;

import java.util.*;
import java.util.function.IntUnaryOperator;

public class Graphs {

    public static class LEdge {
        public final int i, j;
        public final long cost;

        public LEdge(int i, int j, long cost) {
            this.i = i;
            this.j = j;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return String.format("[(%d, %d): %d]", i, j, cost);
        }
    }

    public static class Edge {
        public final int i, j, cost;

        public Edge(int i, int j, int cost) {
            this.i = i;
            this.j = j;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return String.format("[(%d, %d): %d]", i, j, cost);
        }
    }

    public static class BellmanFord {
        public final long[] distance;
        public final int[] predecessor;
        public boolean cycle;

        public BellmanFord(int n, List<Edge> edges, int start) {
            distance = new long[n];
            Arrays.fill(distance, Long.MAX_VALUE);
            predecessor = new int[n];

            distance[start] = 0;

            for (int i = 0; i < n - 1; i++) {
                for (Edge e : edges) {
                    if (distance[e.i] < Long.MAX_VALUE && distance[e.i] + e.cost < distance[e.j]) {
                        distance[e.j] = distance[e.i] + e.cost;
                        predecessor[e.j] = e.i;
                    }
                }
            }

            cycle = false;
            for (Edge e : edges) {
                if (distance[e.i] < Long.MAX_VALUE && distance[e.i] + e.cost < distance[e.j]) {
                    cycle = true;
                    break;
                }
            }
        }
    }

    public static int[] topologicalSortUsingEdgeArray(Edge[] edges, List<Integer>[] adj) {
        List<Integer>[] realAdj = new List[adj.length];
        for (int i = 0; i < adj.length; i++) {
            realAdj[i] = new ArrayList<>();
            for (int j : adj[i]) {
                realAdj[i].add(edges[j].j);
            }
        }
        return topologicalSort(realAdj);
    }

    /**
     * If there is a cycle, the returned array will not contain all vertices.
     */
    public static int[] topologicalSort(List<Integer>[] adj) {
        final int n = adj.length;
        int[] inDegree = new int[n];
        int[] answer = new int[n];
        int size = 0;

        for (int i = 0; i < n; i++) {
            for (int j : adj[i])
                inDegree[j]++;
        }

        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0)
                answer[size++] = i;
        }

        int q = 0;
        while (q < size) {
            int cur = answer[q++];
            for (int next : adj[cur]) {
                if (--inDegree[next] == 0)
                    answer[size++] = next;
            }
        }

        if (size < answer.length)
            return Arrays.copyOf(answer, size);

        return answer;
    }

    public static BitSet reachabilityViaBFS(List<Integer>[] adj, int start) {
        BitSet visited = new BitSet();
        Queue<Integer> q = new ArrayDeque<>();
        q.add(start);
        visited.set(start);

        while (!q.isEmpty()) {
            int cur = q.poll();
            for (int next : adj[cur]) {
                if (visited.get(next))
                    continue;

                q.add(next);
                visited.set(next);
            }
        }

        return visited;
    }

    public static int[] distanceViaBFS(List<Integer>[] adj, int start) {
        final int n = adj.length;
        int[] distance = new int[n];
        Arrays.fill(distance, -1);
        Queue<Integer> q = new ArrayDeque<>();
        q.add(start);
        distance[start] = 0;

        while (!q.isEmpty()) {
            int cur = q.poll();
            for (int next : adj[cur]) {
                if (distance[next] != -1)
                    continue;

                q.add(next);
                distance[next] = distance[cur] + 1;
            }
        }

        return distance;
    }

    public static int[] preorder(List<Integer>[] adj, int root) {
        final int n = adj.length;
        int[] preorder = new int[n];
        int[] stack = new int[n];
        BitSet visited = new BitSet();
        int top = -1;
        visited.set(root);
        stack[++top] = root;

        int index = 0;
        while (top >= 0) {
            int cur = stack[top--];
            preorder[index++] = cur;
            for (int i = adj[cur].size() - 1; i >= 0; i--) {
                int j = adj[cur].get(i);
                if (visited.get(j))
                    continue;

                stack[++top] = j;
                visited.set(j);
            }
        }

        return preorder;
    }

    public static int[] postorder(List<Integer>[] adj, int root) {
        int[] postorder = preorder(adj, root);
        Util.reverse(postorder);
        return postorder;
    }

    public static int[] subtreeSizes(List<Integer>[] adj, int root) {
        final int n = adj.length;
        int[] size = new int[n];

        int[] postorder = postorder(adj, root);
        for (int i : postorder) {
            for (int j : adj[i]) {
                size[i] += size[j];
            }
            size[i]++;
        }

        return size;
    }

    /**
     * vertices are numbered 0 to n-1
     */
    public static List<Edge> findMSTUsingKruskal(int n, List<Edge> edges) {
        edges.sort(Comparator.comparingInt(i -> i.cost));

        UnionFind uf = new UnionFind(n);
        List<Edge> mst = new ArrayList<>();

        for (Edge e : edges) {
            if (uf.rep(e.i) != uf.rep(e.j)) {
                mst.add(e);
                uf.union(e.i, e.j);
            }
        }

        return mst;
    }

    /**
     * vertices are numbered 0 to n-1
     */
    public static List<LEdge> findMSTUsingKruskalLong(int n, List<LEdge> edges) {
        edges.sort(Comparator.comparingLong(i -> i.cost));

        UnionFind uf = new UnionFind(n);
        List<LEdge> mst = new ArrayList<>();

        for (LEdge e : edges) {
            if (uf.rep(e.i) != uf.rep(e.j)) {
                mst.add(e);
                uf.union(e.i, e.j);
            }
        }

        return mst;
    }

    public static Pair<Integer, int[]> scc(List<Integer>[] adj) {
        final int n = adj.length;

        int[] components = new int[n];
        Arrays.fill(components, -1);
        int[] index = new int[n];
        Arrays.fill(index, -1);
        int[] stack = new int[n + 1];
        Arrays.fill(stack, -1);

        IntUnaryOperator tarjan = new IntUnaryOperator() {
            public int c = 0, v = 0, top = 0;

            @Override
            public int applyAsInt(int i) {
                stack[top++] = i;
                int low = index[i] = v++;
                for (int next : adj[i]) {
                    if (index[next] == -1)
                        low = Math.min(low, applyAsInt(next));
                    else if (components[next] == -1)
                        low = Math.min(low, index[next]);
                }
                if (low == index[i]) {
                    while (stack[top] != i)
                        components[stack[--top]] = c;
                    c++;
                }
                return low;
            }
        };

        for (int i = 0; i < n; i++) {
            if (index[i] == -1) {
                tarjan.applyAsInt(i);
            }
        }

        return Pair.of(Util.max(components) + 1, components);
    }
}
