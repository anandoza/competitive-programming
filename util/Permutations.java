package library.util;

import library.InputReader;
import library.numeric.NumberTheory;
import library.segtree.IntSumSegmentTree;

import java.util.*;

import static library.util.Util.ASSERT;

public class Permutations {

    /**
     * Returns the inversion count of a permutation represented as an array with elements in [0, n).
     */
    public static long countInversions(int[] permutation) {
        ASSERT(false);
        return 0;
    }

    public static int[] identity(int n) {
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = i;
        }
        return p;
    }

    public static int[] readPermutation(int n, InputReader in) {
        int[] p = in.readIntArray(n);
        for (int i = 0; i < n; i++) {
            p[i]--;
        }
        return p;
    }

    public static int[] getPermutation(int[] before, int[] after) {
        ASSERT(before.length == after.length);

        HashMap<Integer, Integer> afterIndex = new HashMap<>();
        int n = after.length;
        for (int i = 0; i < n; i++) {
            afterIndex.put(after[i], i);
        }

        int[] permutation = new int[n];
        for (int i = 0; i < n; i++) {
            permutation[i] = afterIndex.remove(before[i]);
        }

        return permutation;
    }

    public static boolean nextPermutation(int[] p) {
        for (int a = p.length - 2; a >= 0; a--) {
            if (p[a] < p[a + 1]) {
                for (int b = p.length - 1; ; b--) {
                    if (p[b] > p[a]) {
                        int t = p[a];
                        p[a] = p[b];
                        p[b] = t;
                        for (a++, b = p.length - 1; a < b; a++, b--) {
                            t = p[a];
                            p[a] = p[b];
                            p[b] = t;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static <T> int[] getPermutation(T[] before, T[] after) {
        ASSERT(before.length == after.length);

        HashMap<T, Integer> afterIndex = new HashMap<>();
        int n = after.length;
        for (int i = 0; i < n; i++) {
            afterIndex.put(after[i], i);
        }

        int[] permutation = new int[n];
        for (int i = 0; i < n; i++) {
            permutation[i] = afterIndex.remove(before[i]);
        }

        return permutation;
    }

    public static int[] getPermutation(char[] before, char[] after) {
        ASSERT(before.length == after.length);

        HashMap<Character, Integer> afterIndex = new HashMap<>();
        int n = after.length;
        for (int i = 0; i < n; i++) {
            afterIndex.put(after[i], i);
        }

        int[] permutation = new int[n];
        for (int i = 0; i < n; i++) {
            permutation[i] = afterIndex.remove(before[i]);
        }

        return permutation;
    }

    public static boolean isPermutation(int[] p) {
        HashSet<Integer> s = new HashSet<>();
        for (int x : p) {
            boolean add = s.add(x);
            if (!add)
                return false;
        }
        return true;
    }

    public static int[] inverse(int[] permutation) {
        int n = permutation.length;
        int[] inverse = new int[n];
        for (int i = 0; i < n; i++) {
            inverse[permutation[i]] = i;
        }
        return inverse;
    }

    public static boolean isOdd(int[] permutation) {
        return isOdd(findCycles(permutation));
    }

    public static boolean isOdd(List<List<Integer>> cycles) {
        boolean parity = false;
        for (List<Integer> c : cycles) {
            parity ^= (c.size() % 2 == 0);
        }
        return parity;
    }

    public static List<List<Integer>> findCycles(int[] permutation) {
        List<List<Integer>> cycles = new ArrayList<>();
        int n = permutation.length;
        BitSet visited = new BitSet(n);

        for (int i = 0; i < n; i++) {
            if (visited.get(i))
                continue;

            List<Integer> c = new ArrayList<>();
            for (int j = i; c.isEmpty() || j != i; j = permutation[j]) {
                c.add(j);
                visited.set(j);
            }

            if (c.size() > 1) {
                cycles.add(c);
            }
        }

        return cycles;
    }

    public static int[] fromCycles(int n, List<List<Integer>> cycles) {
        int[] p = new int[n];
        Arrays.fill(p, -1);
        for (List<Integer> c : cycles) {
            for (int i = 0; i + 1 < c.size(); i++) {
                p[c.get(i)] = c.get(i + 1);
            }
            p[c.get(c.size() - 1)] = c.get(0);
        }
        for (int i = 0; i < n; i++) {
            if (p[i] == -1)
                p[i] = i;
        }
        return p;
    }

    /**
     * p has duplicates, find lexicographic rank among all permutations of p
     * <p>
     * The first one is rank = 0.
     */
    public static long rank(int[] p, NumberTheory.Modulus mod) {
        TreeSet<Integer> distinct = new TreeSet<>();
        for (int i : p)
            distinct.add(i);

        HashMap<Integer, Integer> inv = new HashMap<>();
        int MAX = 0;
        for (int i : distinct) {
            inv.put(i, MAX++);
        }

        int[] pOriginal = p;
        final int n = pOriginal.length;
        p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = inv.get(pOriginal[i]);
        }

        int[] count = new int[MAX];
        for (int i = 0; i < n; i++) {
            count[p[i]]++;
        }

        IntSumSegmentTree sum = new IntSumSegmentTree(MAX);
        for (int i = 0; i < MAX; i++) {
            sum.update_LAZY(i, count[i]);
        }
        sum.rebuild();

        long rank = 0;
        long total = mod.fact(n);
        for (int i = 0; i < MAX; i++) {
            total = mod.mult(total, mod.fInv(count[i]));
        }

        long[] modInv = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            modInv[i] = mod.inv(i);
        }
        for (int i = 0; i < n; i++) {
            int cur = p[i];
            long earlier = 0;
            long less = sum.query(0, cur);
            earlier = mod.add(earlier, mod.mult(total, less, modInv[n - i]));
            rank = mod.add(rank, earlier);
            total = mod.mult(total, sum.get(cur), modInv[n - i]);
            sum.update(cur, sum.get(cur) - 1);
        }

        return rank;
    }

    public static int[] square(int[] p) {
        final int n = p.length;
        int[] s = new int[n];
        for (int i = 0; i < n; i++) {
            s[i] = p[p[i]];
        }
        return s;
    }

    public static void main(String[] args) {
        int[] permutation = {0, 2, 5, 4, 3, 1};
        System.out.println(Arrays.toString(permutation));
        System.out.println(findCycles(permutation));
        System.out.println(isOdd(permutation));
        System.out.println(Arrays.toString(inverse(permutation)));
        System.out.println(findCycles(inverse(permutation)));

        System.out.println("Rank: " + rank(permutation, new NumberTheory.Mod107()));
        System.out.println("Rank: " + rank(new int[]{1, 0, 1}, new NumberTheory.Mod107()));
        System.out.println("Rank: " + rank(new int[]{1, 1, 0}, new NumberTheory.Mod107()));

        System.out.println(Arrays.toString(getPermutation("constaz".toCharArray(), "tonaczs".toCharArray())));

        int[] p = identity(3);
        do {
            System.out.println(Arrays.toString(p) + " " + Arrays.toString(square(p)));
        } while (nextPermutation(p));
    }

    private Permutations() {
    }
}
