package library.util;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;
import java.util.function.Predicate;

public class Util {
    public static String formatDouble(double x) {
        return String.format("%.15f", x);
    }

    public static void print2DArray(int[][] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null)
                sb.append("null");
            else
                for (int j = 0; j < array[i].length; j++) {
                    sb.append(String.format("%5d ", array[i][j]));
                }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public static void print2DArray(boolean[][] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null)
                sb.append("null");
            else
                for (int j = 0; j < array[i].length; j++) {
                    sb.append(String.format("%5d ", array[i][j] ? 1 : 0));
                }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public static void print2DArray(Object[][] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                sb.append(String.format("%5s ", array[i][j]));
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public static void print2DArray(long[][] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                sb.append(String.format("%5d ", array[i][j]));
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public static String join(Iterable i) {
        StringBuilder sb = new StringBuilder();
        for (Object o : i) {
            sb.append(o);
            sb.append(" ");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String join(int... i) {
        StringBuilder sb = new StringBuilder();
        for (int o : i) {
            sb.append(o);
            sb.append(" ");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String join(String... i) {
        StringBuilder sb = new StringBuilder();
        for (Object o : i) {
            sb.append(o);
            sb.append(" ");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String join(Object[] i) {
        StringBuilder sb = new StringBuilder();
        for (Object o : i) {
            sb.append(o);
            sb.append(" ");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String join(long... i) {
        StringBuilder sb = new StringBuilder();
        for (long o : i) {
            sb.append(o);
            sb.append(" ");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static double min(double[] x) {
        double min = Double.POSITIVE_INFINITY;
        for (double i : x) {
            min = Math.min(i, min);
        }
        return min;
    }

    public static double max(double[] x) {
        double max = Double.NEGATIVE_INFINITY;
        for (double i : x) {
            max = Math.max(i, max);
        }
        return max;
    }

    public static long max(long... x) {
        long max = Long.MIN_VALUE;
        for (long i : x) {
            max = Math.max(i, max);
        }
        return max;
    }

    public static long min(long... x) {
        long min = Long.MAX_VALUE;
        for (long i : x) {
            min = Math.min(i, min);
        }
        return min;
    }

    public static int max(int... x) {
        int max = Integer.MIN_VALUE;
        for (int i : x) {
            max = Math.max(i, max);
        }
        return max;
    }

    public static int min(int... x) {
        int min = Integer.MAX_VALUE;
        for (int i : x) {
            min = Math.min(i, min);
        }
        return min;
    }

    public static void safeSort(int[] x) {
        shuffle(x);
        Arrays.sort(x);
    }

    public static void shuffle(int[] x) {
        Random r = new Random();

        for (int i = 0; i <= x.length - 2; i++) {
            int j = i + r.nextInt(x.length - i);
            swap(x, i, j);
        }
    }

    public static void safeSort(long[] x) {
        shuffle(x);
        Arrays.sort(x);
    }

    public static void shuffle(long[] x) {
        Random r = new Random();

        for (int i = 0; i <= x.length - 2; i++) {
            int j = i + r.nextInt(x.length - i);
            swap(x, i, j);
        }
    }

    public static void swap(char[] x, int i, int j) {
        char t = x[i];
        x[i] = x[j];
        x[j] = t;
    }

    public static void swap(int[] x, int i, int j) {
        int t = x[i];
        x[i] = x[j];
        x[j] = t;
    }

    public static <T> void swap(T[] x, int i, int j) {
        T t = x[i];
        x[i] = x[j];
        x[j] = t;
    }

    public static void swap(long[] x, int i, int j) {
        long t = x[i];
        x[i] = x[j];
        x[j] = t;
    }

    public static void reverse(int[] x) {
        for (int i = 0, j = x.length - 1; i < j; i++, j--) {
            swap(x, i, j);
        }
    }

    public static void reverse(char[] x) {
        for (int i = 0, j = x.length - 1; i < j; i++, j--) {
            swap(x, i, j);
        }
    }

    public static void reverse(long[] x) {
        for (int i = 0, j = x.length - 1; i < j; i++, j--) {
            swap(x, i, j);
        }
    }

    public static <T> void reverse(T[] x) {
        for (int i = 0, j = x.length - 1; i < j; i++, j--) {
            swap(x, i, j);
        }
    }

    public static double error(double a, double b) {
        return Math.abs(a - b) / Math.max(1, Math.min(Math.abs(a), Math.abs(b)));
    }

    public static long nc2(long n) {
        return n * (n - 1) / 2;
    }

    public static void ASSERT(boolean assertion) {
        if (!assertion)
            throw new AssertionError();
    }

    public static void ASSERT(boolean assertion, String message) {
        if (!assertion)
            throw new AssertionError(message);
    }

    private Util() {
    }

    public static void main(String[] args) {
        double x = 0.0000000001;
        System.out.println(x);
        System.out.format("%.15f%n", x);
        System.out.format("%f%n", x);
    }

    @SuppressWarnings("UseCompareMethod")
    public static int sign(int x) {
        return x > 0 ? 1 : x == 0 ? 0 : -1;
    }

    public static int sign(long x) {
        return x > 0 ? 1 : x == 0 ? 0 : -1;
    }

    public static int[] prefixSum(int[] x) {
        int[] s = new int[x.length + 1];
        for (int i = 0; i < x.length; i++) {
            s[i + 1] = s[i] + x[i];
        }
        return s;
    }

    public static long[] prefixSum(long[] x) {
        long[] s = new long[x.length + 1];
        for (int i = 0; i < x.length; i++) {
            s[i + 1] = s[i] + x[i];
        }
        return s;
    }

    public static int[] cumulativeSumWithoutZero(int[] x) {
        int[] s = new int[x.length];
        s[0] = x[0];
        for (int i = 1; i < x.length; i++) {
            s[i] = s[i - 1] + x[i];
        }
        return s;
    }

    public static long[] cumulativeSumWithoutZero(long[] x) {
        long[] s = new long[x.length];
        s[0] = x[0];
        for (int i = 1; i < x.length; i++) {
            s[i] = s[i - 1] + x[i];
        }
        return s;
    }

    public static long kadane(int[] a) {
        long max = 0, sum = 0;
        for (int i : a) {
            sum += i;
            if (sum < 0)
                sum = 0;
            max = Math.max(max, sum);
        }
        return max;
    }

    /**
     * [0, returnValue) is where the predicate is true.
     */
    public static <T> int partition(T[] a, Predicate<T> first) {
        return partition(a, first, 0, a.length);
    }

    /**
     * Partitions [l, r).
     * <p>
     * [l, returnValue) is where the predicate is true.
     */
    public static <T> int partition(T[] a, Predicate<T> first, int l, int r) {
        while (l < r && first.test(a[l])) {
            l++;
        }
        if (l == r)
            return l;
        for (int i = l + 1; i < r; i++) {
            if (first.test(a[i])) {
                Util.swap(a, i, l);
                l++;
            }
        }
        return l;
    }

    public static int mex(int... x) {
        BitSet bs = new BitSet();
        for (int i : x)
            bs.set(i);
        return bs.nextClearBit(0);
    }

    public static int mex(Iterable<Integer> x) {
        BitSet bs = new BitSet();
        for (int i : x)
            bs.set(i);
        return bs.nextClearBit(0);
    }
}
