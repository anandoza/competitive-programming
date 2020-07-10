package library.numeric.matrix;

public class BitMatrix32 {
    final int rows, cols;
    public final int[] data;

    public BitMatrix32(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        data = new int[rows];
    }

    /**
     * Returns the rank.
     */
    public int reduce() {
        return reducePartial(0, rows);
    }

    /**
     * Returns the rank.
     * <p>
     * maxRank is an upper bound on rank (the last (rows-rank) rows should be all zeros).
     */
    public int reducePartial(int alreadyReducedRowCount, int maxRank) {
        int firstZeroRow = maxRank;
        for (int i = 0; i < firstZeroRow; i++) {
            while (data[i] == 0) {
                if (firstZeroRow - 1 >= i) {
                    swapRows(i, firstZeroRow - 1);
                    firstZeroRow--;
                } else {
                    return firstZeroRow;
                }
            }
            int firstNonZeroCol = Math.min(cols, Integer.numberOfTrailingZeros(data[i]));

            for (int b = Math.max(alreadyReducedRowCount, i + 1); b < firstZeroRow; b++) {
                if (((data[b] >> firstNonZeroCol) & 1) != 0) {
                    xorRow(b, i);
                }
            }
        }

        return firstZeroRow;
    }

    public void swapRows(int i, int j) {
        int t = data[i];
        data[i] = data[j];
        data[j] = t;
    }

    public void xorRow(int target, int rowToAdd) {
        data[target] ^= data[rowToAdd];
    }
}
