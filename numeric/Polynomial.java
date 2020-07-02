package library.numeric;

import library.util.Util;

import java.util.Arrays;

import static library.numeric.NumberTheory.Modulus;
import static library.util.Util.ASSERT;

public class Polynomial<M extends Modulus<M>> {
    public final long[] coeff;
    public final int n; // degree
    public final M m;

    public Polynomial(int degree, M modulus, long... coeff) {
        m = modulus;
        n = degree;
        this.coeff = new long[n + 1];
        for (int i = 0; i < Math.min(this.coeff.length, coeff.length); i++) {
            this.coeff[i] = coeff[i];
        }
    }

    public Polynomial(M modulus, long... coeff) {
        m = modulus;
        n = coeff.length - 1;
        this.coeff = new long[n + 1];
        for (int i = 0; i <= n; i++) {
            this.coeff[i] = coeff[i];
        }
    }

    public long eval(long x) {
        long result = 0;
        for (int i = n; i >= 0; i--) {
            result = m.mult(result, x);
            result = m.add(result, coeff[i]);
        }
        return result;
    }

    public Polynomial<M> mult(Polynomial<M> other) {
        long[] result = new long[n + other.n + 1];
        long threshold = Long.MAX_VALUE - m.modulus() * m.modulus();
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= other.n; j++) {
                result[i + j] += coeff[i] * other.coeff[j];
                if (result[i + j] >= threshold) {
                    result[i + j] %= m.modulus();
                }
            }
        }
        for (int i = 0; i < result.length; i++) {
            result[i] %= m.modulus();
        }
        return m.create(result);
    }

    public Polynomial<M> powFFT(int e) {
        if (e == 0)
            return m.create(new long[]{1});
        if (e == 1)
            return this;
        if ((e & 1) > 0)
            return this.multFFT(this.powFFT(e - 1));
        Polynomial<M> half = this.powFFT(e / 2);
        return half.multFFT(half);
    }

    public Polynomial<M> multFFT(Polynomial<M> other) {
        if (Math.min(n, other.n) < 100) {
            return this.mult(other);
        }

        int resultDegree = n + other.n;
        int resultLength = resultDegree + 1;
        int resultLengthBig = Integer.highestOneBit(resultLength);
        if (resultLengthBig == resultLength)
            resultLengthBig *= 2;
        resultLengthBig *= 2;
        int resultDegreeBig = resultLengthBig - 1;

        boolean eq = Arrays.equals(coeff, other.coeff);
        Polynomial<M> a = new Polynomial<>(resultDegreeBig, m, coeff);
        a.inPlaceFFT(false);
        if (!eq) {
            Polynomial<M> b = new Polynomial<>(resultDegreeBig, m, other.coeff);
            b.inPlaceFFT(false);
            for (int i = 0; i < a.coeff.length; i++) {
                a.coeff[i] = m.mult(a.coeff[i], b.coeff[i]);
            }
        } else {
            for (int i = 0; i < a.coeff.length; i++) {
                a.coeff[i] = m.mult(a.coeff[i], a.coeff[i]);
            }
        }
        a.inPlaceFFT(true);
        return new Polynomial<>(resultDegree, m, a.coeff);
    }

    public void inPlaceFFT(boolean inverse) {
        ASSERT(Integer.bitCount(n + 1) == 1);

        for (int i = 1, j = 0; i < n + 1; i++) {
            for (int k = (n + 1) >> 1; (j ^= k) < k; k >>= 1)
                ;

            if (i < j)
                Util.swap(coeff, i, j);
        }

        for (int l = 1; l < n + 1; l <<= 1) {
            long[] unityRoots = new long[2 * l + 1];
            unityRoots[0] = 1;
            unityRoots[1] = m.unityRoot(2 * l);
            for (int i = 2; i < unityRoots.length; i++) {
                unityRoots[i] = m.mult(unityRoots[i - 1], unityRoots[1]);
            }
            for (int i = 0; i < n + 1; i += 2 * l) {
                for (int j = 0; j < l; j++) {
                    int wIndex = inverse ? 2 * l - j : j;
                    long w = unityRoots[wIndex];
                    long u = coeff[i + j];
                    long v = m.mult(coeff[i + j + l], w);
                    coeff[i + j] = m.add(u, v);
                    coeff[i + j + l] = m.subtract(u, v);
                }
            }
        }

        if (inverse) {
            long nInv = m.inv(n + 1);
            for (int i = 0; i < n + 1; i++) {
                coeff[i] = m.mult(coeff[i], nInv);
            }
        }
    }

    public Polynomial<M> add(Polynomial<M> other) {
        int degree = Math.max(n, other.n);
        long[] result = new long[degree + 1];
        for (int i = 0; i <= n; i++) {
            result[i] = m.add(result[i], coeff[i]);
        }
        for (int i = 0; i <= other.n; i++) {
            result[i] = m.add(result[i], other.coeff[i]);
        }
        return m.create(result);
    }

    public Polynomial<M> mult(long scale) {
        long[] result = new long[n + 1];
        for (int i = 0; i <= n; i++) {
            result[i] = m.mult(coeff[i], scale);
        }
        return m.create(result);
    }

    public Polynomial<M> div(Polynomial<M> other) {
        long[] result = new long[n - other.n + 1];

        ASSERT(false); // TODO
        return null;
    }

    public Polynomial<M> truncateDegree(int newDegree) {
        if (newDegree >= n)
            return this;
        long[] result = new long[newDegree + 1];
        System.arraycopy(this.coeff, 0, result, 0, result.length);
        return m.create(result);
    }

    @Override
    public String toString() {
        return Arrays.toString(coeff);
    }

    public static void main(String[] args) {
        Modulus m = new NumberTheory.Mod998();
        Polynomial p = m.create(1, 2, 3);
        System.out.println(p);
        System.out.println(p.mult(p));
        System.out.println(p.multFFT(p));
        System.out.println(p.multFFT(p).multFFT(p).multFFT(p).multFFT(p));
        System.out.println(p.powFFT(5));
        for (int x = 0; x < 10; x++) {
            System.out.println(p.eval(x));
        }
    }
}
