package library.numeric.matrix;

import library.numeric.NumberTheory.Mod107;
import library.numeric.NumberTheory.Modulus;

import static library.util.Util.ASSERT;

public class ModularMatrix {
    final int rows, cols;
    final Modulus m;
    public long[][] data;

    public ModularMatrix(int rows, int cols, Modulus m) {
        this.rows = rows;
        this.cols = cols;
        this.m = m;
        data = new long[rows][cols];
    }

    public ModularMatrix add(ModularMatrix other) {
        ASSERT(this.m.modulus() == other.m.modulus());
        ASSERT(this.rows == other.rows);
        ASSERT(this.cols == other.cols);

        ModularMatrix result = new ModularMatrix(rows, cols, m);

        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                result.data[i][j] = m.add(data[i][j], other.data[i][j]);
            }
        }

        return result;
    }

    public ModularMatrix mult(ModularMatrix other) {
        ASSERT(this.m.modulus() == other.m.modulus());
        ASSERT(this.cols == other.rows);

        ModularMatrix result = new ModularMatrix(this.rows, other.cols, m);

        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                result.data[i][j] = 0;
                for (int k = 0; k < this.cols; k++) {
                    result.data[i][j] = m.add(result.data[i][j], m.mult(this.data[i][k], other.data[k][j]));
                }
            }
        }

        return result;
    }

    public ModularMatrix pow(int e) {
        ASSERT(cols == rows);

        ASSERT(e >= 0);
        if (e == 0)
            return identity(rows, m);
        if ((e & 1) > 0)
            return mult(pow(e - 1));
        return mult(this).pow(e / 2);
    }

    public ModularMatrix pow(long e) {
        ASSERT(cols == rows);

        ASSERT(e >= 0);
        if (e == 0)
            return identity(rows, m);
        if ((e & 1) > 0)
            return mult(pow(e - 1));
        return mult(this).pow(e / 2);
    }

    public static ModularMatrix identity(int size, Modulus m) {
        ModularMatrix r = new ModularMatrix(size, size, m);
        for (int i = 0; i < size; i++) {
            r.data[i][i] = 1;
        }
        return r;
    }

    /**
     * Returns the rank.
     */
    public int reduce() {
        int firstZeroRow = rows;
        for (int i = 0; i < firstZeroRow; i++) {
            int firstNonZeroCol = cols;
            while (firstNonZeroCol == cols) {
                for (int j = 0; j < cols; j++) {
                    if (data[i][j] != 0) {
                        firstNonZeroCol = j;
                        break;
                    }
                }

                if (firstNonZeroCol == cols) {
                    if (firstZeroRow - 1 >= i) {
                        swapRows(i, firstZeroRow - 1);
                        firstZeroRow--;
                    } else {
                        return firstZeroRow;
                    }
                }
            }

            scaleRow(i, m.inv(data[i][firstNonZeroCol]));
            for (int b = i + 1; b < firstZeroRow; b++) {
                long factor = m.negative(data[b][firstNonZeroCol]);
                addMultipleOfRow(b, i, factor);
            }
        }

        return firstZeroRow;
    }

    /**
     * Returns the rank.
     * <p>
     * <p>
     * maxRank is an upper bound on rank (the last (rows-rank) rows should be all zeros).
     */
    public int reducePartial(int alreadyReducedRowCount, int maxRank) {
        int firstZeroRow = maxRank;
        for (int i = 0; i < firstZeroRow; i++) {
            int firstNonZeroCol = cols;
            while (firstNonZeroCol == cols) {
                for (int j = 0; j < cols; j++) {
                    if (data[i][j] != 0) {
                        firstNonZeroCol = j;
                        break;
                    }
                }

                if (firstNonZeroCol == cols) {
                    if (firstZeroRow - 1 >= i) {
                        swapRows(i, firstZeroRow - 1);
                        firstZeroRow--;
                    } else {
                        return firstZeroRow;
                    }
                }
            }

            scaleRow(i, m.inv(data[i][firstNonZeroCol]));
            for (int b = Math.max(alreadyReducedRowCount, i + 1); b < firstZeroRow; b++) {
                long factor = m.negative(data[b][firstNonZeroCol]);
                addMultipleOfRow(b, i, factor);
            }
        }

        return firstZeroRow;
    }

    public void swapRows(int i, int j) {
        long[] t = data[i];
        data[i] = data[j];
        data[j] = t;
    }

    public void scaleRow(int i, long factor) {
        for (int j = 0; j < cols; j++) {
            data[i][j] = m.mult(data[i][j], factor);
        }
    }

    public void addMultipleOfRow(int target, int rowToAdd, long factor) {
        for (int j = 0; j < cols; j++) {
            data[target][j] = m.add(data[target][j], m.mult(data[rowToAdd][j], factor));
        }
    }

    public static class MatrixModulus<M extends Modulus<M>> {
        public final M mod;

        public MatrixModulus(M mod) {
            this.mod = mod;
        }
    }

    public static class MatrixDimension {
        final int n, m;

        public MatrixDimension(int n, int m) {
            this.n = n;
            this.m = m;
        }
    }

    public static class MatrixModulusAndDimension {
        public final int n, m;
        public final Modulus mod;

        public MatrixModulusAndDimension(int n, int m, Modulus mod) {
            this.n = n;
            this.m = m;
            this.mod = mod;
        }

        public ModularMatrix identity() {
            ASSERT(n == m);

            ModularMatrix mat = new ModularMatrix(n, n, mod);
            for (int i = 0; i < n; i++) {
                mat.data[i][i] = 1;
            }
            return mat;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int maxLen = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maxLen = Math.max(maxLen, String.valueOf(data[i][j]).length());
            }
        }
        String format = String.format("%%%dd ", maxLen + 1);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(String.format(format, data[i][j]));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        Mod107 m = new Mod107();
        ModularMatrix matrix = new ModularMatrix(3, 4, m);
        matrix.data = new long[][]{{1, 3, 1, 9}, {1, 1, -1, 1}, {3, 11, 5, 35}};
        System.out.println(matrix);
        System.out.println(matrix.reduce());
        System.out.println(matrix);
        matrix.data[2] = new long[]{3, 11, 5, 35};
        System.out.println(matrix);
    }
}
