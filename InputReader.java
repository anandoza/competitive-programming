package library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class InputReader {
    public BufferedReader reader;
    public StringTokenizer tokenizer;

    public InputReader(InputStream stream) {
        reader = new BufferedReader(new InputStreamReader(stream), 32768);
        tokenizer = null;
    }

    public String next() {
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            try {
                tokenizer = new StringTokenizer(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return tokenizer.nextToken();
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    public long nextLong() {
        return Long.parseLong(next());
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }

    public int[] readIntArray(int n) {
        int[] x = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = nextInt();
        }
        return x;
    }

    public int[] readBitString() {
        char[] chars = next().toCharArray();
        int[] x = new int[chars.length];
        for (int i = 0; i < x.length; i++) {
            x[i] = chars[i] - '0';
        }
        return x;
    }

    public Integer[] readIntegerArray(int n) {
        Integer[] x = new Integer[n];
        for (int i = 0; i < n; i++) {
            x[i] = nextInt();
        }
        return x;
    }

    public long[] readLongArray(int n) {
        long[] x = new long[n];
        for (int i = 0; i < n; i++) {
            x[i] = nextLong();
        }
        return x;
    }

    public Long[] readLongObjectArray(int n) {
        Long[] x = new Long[n];
        for (int i = 0; i < n; i++) {
            x[i] = nextLong();
        }
        return x;
    }

    public double[] readDoubleArray(int n) {
        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = nextDouble();
        }
        return x;
    }

    public String nextLine() {
        return null;
    }
}