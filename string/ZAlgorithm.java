package library.string;

public class ZAlgorithm {
    private ZAlgorithm() {
    }

    /**
     * Returns z[i] = greatest length L s.t. s[i, i+L) = s[offset, offset + L) and i >= offset.
     */
    public static int[] zAlgorithm(char[] s, int offset) {
        int n = s.length - offset;
        int[] z = new int[n];
        z[0] = n;

        for (int i = 1, l = -1, r = -1; i < n; i++) {
            z[i] = r > i ? Math.min(r - i, z[i - l]) : 0;
            while (i + z[i] < n && s[offset + i + z[i]] == s[offset + z[i]])
                z[i]++;
            if (i + z[i] > r) {
                l = i;
                r = i + z[i];
            }
        }
        return z;
    }

    /**
     * Returns z[i] = greatest length L s.t. s[i, i+L) = s[0, L).
     */
    public static int[] zAlgorithm(char[] s) {
        int n = s.length;
        int[] z = new int[n];
        z[0] = n;

        for (int i = 1, l = -1, r = -1; i < n; i++) {
            z[i] = r > i ? Math.min(r - i, z[i - l]) : 0;
            while (i + z[i] < n && s[i + z[i]] == s[z[i]])
                z[i]++;
            if (i + z[i] > r) {
                l = i;
                r = i + z[i];
            }
        }
        return z;
    }
}
