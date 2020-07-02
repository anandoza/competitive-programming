package library.numeric;

import library.util.Util;

import java.util.ArrayList;

public class NumberTheory {

    public static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        int c;
        while (a != 0) {
            c = a;
            a = b % a;
            b = c;
        }
        return b;
    }

    public static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        long c;
        while (a != 0) {
            c = a;
            a = b % a;
            b = c;
        }
        return b;
    }

    public static long lcm(long a, long b) {
        return (a / gcd(a, b)) * b;
    }

    public static int powInt(int x, int e) {
        if (e == 0)
            return 1;
        if ((e & 1) > 0)
            return x * powInt(x, e - 1);
        return powInt(x * x, e / 2);
    }

    public static long pow(long x, long e) {
        if (e == 0)
            return 1;
        if ((e & 1) > 0)
            return x * pow(x, e - 1);
        return pow(x * x, e / 2);
    }

    public static long crt(Modulus[] mods, long[] residues) {
        Util.ASSERT(mods.length == residues.length);
        final int m = mods.length;

        long[] x = new long[m];
        for (int i = 0; i < m; i++) {
            x[i] = residues[i];
            for (int j = 0; j < i; j++) {
                x[i] = mods[i].mult(mods[i].subtract(x[i], x[j]), mods[i].inv(mods[i].normalize(mods[j].modulus())));
            }
        }

        long v = 0;
        for (int i = m - 1; i >= 0; i--) {
            v = mods[i].modulus() * v + x[i];
        }
        return v;
    }

    public static long[] crt(Modulus[] mods, long[][] residues) {
        Util.ASSERT(mods.length == residues[0].length);
        final int m = mods.length;

        long[] result = new long[residues.length];
        long[][] inverses = new long[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                if (i == j)
                    continue;
                inverses[i][j] = mods[i].inv(mods[i].normalize(mods[j].modulus()));
            }
        }
        for (int k = 0; k < residues.length; k++) {
            long[] x = new long[m];
            for (int i = 0; i < m; i++) {
                x[i] = residues[k][i];
                for (int j = 0; j < i; j++) {
                    x[i] = mods[i].mult(mods[i].subtract(x[i], x[j]), inverses[i][j]);
                }
            }

            long v = 0;
            for (int i = m - 1; i >= 0; i--) {
                v = mods[i].modulus() * v + x[i];
            }
            result[k] = v;
        }

        return result;
    }

    public static long totient(long n) {
        long tot = n;
        for (long p = 2; p * p <= n; p++)
            if (n % p == 0) {
                tot = tot / p * (p - 1);
                while (n % p == 0)
                    n /= p;
            }
        if (n > 1)
            tot = tot / n * (n - 1);
        return tot;
    }

    public abstract static class Modulus<M extends Modulus<M>> {
        public abstract long modulus();

        ArrayList<Long> factorial = new ArrayList<>();
        ArrayList<Long> invFactorial = new ArrayList<>();

        public Modulus() {
            super();
            factorial.add(1L);
            invFactorial.add(1L);
        }

        public long fact(int n) {
            while (factorial.size() <= n) {
                factorial.add(mult(factorial.get(factorial.size() - 1), factorial.size()));
            }

            return factorial.get(n);
        }

        public long fInv(int n) {
            int lastKnown = invFactorial.size() - 1;

            if (lastKnown < n) {
                long[] fInv = new long[n - lastKnown];
                fInv[0] = inv(fact(n));
                for (int i = 1; i < fInv.length; i++) {
                    fInv[i] = mult(fInv[i - 1], n - i + 1);
                }
                for (int i = fInv.length - 1; i >= 0; i--) {
                    invFactorial.add(fInv[i]);
                }
            }

            return invFactorial.get(n);
        }

        public long derangements(int n) {
            fact(n);
            long result = 0;
            for (int i = 0; i <= n; i++) {
                if (i % 2 == 0)
                    result = add(result, fInv(i));
                else
                    result = subtract(result, fInv(i));
            }
            result = mult(result, fact(n));

            return result;
        }

        public long ncr(int n, int r) {
            ASSERT(n >= 0);
            if (r < 0 || n < r)
                return 0;
            return mult(fact(n), mult(fInv(r), fInv(n - r)));
        }

        public long nPermuteR(int n, int r) {
            ASSERT(n >= 0);
            if (r < 0 || n < r)
                return 0;
            return mult(fact(n), fInv(n - r));
        }

        public long normalize(long x) {
            x %= modulus();
            if (x < 0)
                x += modulus();
            return x;
        }

        public long add(long... x) {
            long r = 0;
            for (long i : x)
                r = add(r, i);
            return r;
        }

        public long add(long a, long b) {
            long v = a + b;
            return v < modulus() ? v : v - modulus();
        }

        public long subtract(long a, long b) {
            long v = a - b;
            return v < 0 ? v + modulus() : v;
        }

        public long mult(long... x) {
            long r = 1;
            for (long i : x)
                r = mult(r, i);
            return r;
        }

        public long mult(long a, long b) {
            return (a * b) % modulus();
        }

        public long div(long a, long b) {
            return mult(a, inv(b));
        }

        public long negative(long x) {
            return x == 0 ? 0 : modulus() - x;
        }

        public long pow(long x, int e) {
            if (e < 0) {
                x = inv(x);
                e *= -1;
            }
            if (e == 0)
                return 1;
            if ((e & 1) > 0)
                return mult(x, pow(x, e - 1));
            return pow(mult(x, x), e / 2);
        }

        public long pow(long x, long e) {
            if (e < 0) {
                x = inv(x);
                e *= -1;
            }
            if (e == 0)
                return 1;
            if ((e & 1) > 0)
                return mult(x, pow(x, e - 1));
            return pow(mult(x, x), e / 2);
        }

        public long inv(long value) {
            long g = modulus(), x = 0, y = 1;
            for (long r = value; r != 0; ) {
                long q = g / r;
                g %= r;

                long temp = g;
                g = r;
                r = temp;

                x -= q * y;

                temp = x;
                x = y;
                y = temp;
            }

            ASSERT(g == 1);
            ASSERT(y == modulus() || y == -modulus());

            return normalize(x);
        }

        public ModularNumber<M> create(long value) {
            return new ModularNumber(value, this);
        }

        public Polynomial<M> create(long... coeff) {
            return new Polynomial(this, coeff);
        }

        public long totient() {
            throw new UnsupportedOperationException("need to implement this");
        }

        public long generator() {
            throw new UnsupportedOperationException("need to implement this");
        }

        public long unityRoot(int degree) {
            ASSERT(totient() % degree == 0);
            return pow(generator(), totient() / degree);
        }
    }

    public static class Mod107 extends Modulus<Mod107> {
        @Override
        public long modulus() {
            return 1_000_000_007L;
        }

        @Override
        public long totient() {
            return modulus() - 1;
        }

        @Override
        public long generator() {
            return 5;
        }
    }

    public static class Mod998 extends Modulus<Mod998> {
        @Override
        public long modulus() {
            return 998_244_353L;
        }

        @Override
        public long totient() {
            return modulus() - 1;
        }

        @Override
        public long generator() {
            return 3;
        }
    }

    public static class SpecialMod469 extends Modulus<SpecialMod469> {
        @Override
        public long modulus() {
            return 469762049;
        }

        @Override
        public long totient() {
            return modulus() - 1;
        }

        @Override
        public long generator() {
            return 3;
        }
    }

    public static class ModM extends Modulus<ModM> {
        private final long modulus;

        public ModM(long modulus) {
            this.modulus = modulus;
        }

        @Override
        public long modulus() {
            return modulus;
        }
    }

    public static class ModularNumber<M extends Modulus<M>> {
        public final long value;
        public final M m;

        public ModularNumber(long value, M m) {
            this.m = m;
            value = value % m.modulus();
            if (value < 0)
                value += m.modulus();
            this.value = value;
        }

        public ModularNumber<M> add(ModularNumber<M> other) {
            ASSERT(m.modulus() == other.m.modulus());
            return this.add(other.value);
        }

        public ModularNumber<M> add(long other) {
            return m.create(value + other);
        }

        public ModularNumber<M> subtract(ModularNumber<M> other) {
            ASSERT(m.modulus() == other.m.modulus());
            return this.subtract(other.value);
        }

        public ModularNumber<M> subtract(long other) {
            return m.create(value - other);
        }

        public ModularNumber<M> mult(ModularNumber<M> other) {
            ASSERT(m.modulus() == other.m.modulus());
            return this.mult(other.value);
        }

        public ModularNumber<M> mult(long other) {
            return m.create(value * other);
        }

        public ModularNumber<M> div(ModularNumber<M> other) {
            ASSERT(m.modulus() == other.m.modulus());
            return this.mult(other.inv());
        }

        public ModularNumber<M> div(long other) {
            return this.div(m.create(other));
        }

        public ModularNumber<M> negative() {
            return m.create(-value);
        }

        public ModularNumber<M> pow(int e) {
            if (e == 0)
                return m.create(1);
            if ((e & 1) > 0)
                return this.mult(this.pow(e - 1));
            return (this.mult(this)).pow(e / 2);
        }

        public ModularNumber<M> pow(long e) {
            if (e == 0)
                return m.create(1);
            if ((e & 1) > 0)
                return this.mult(this.pow(e - 1));
            return (this.mult(this)).pow(e / 2);
        }

        public ModularNumber<M> inv() {
            long g = m.modulus(), x = 0, y = 1;
            for (long r = value; r != 0; ) {
                long q = g / r;
                g %= r;

                long temp = g;
                g = r;
                r = temp;

                x -= q * y;

                temp = x;
                x = y;
                y = temp;
            }

            ASSERT(g == 1);
            ASSERT(y == m.modulus() || y == -m.modulus());

            return m.create(x);
        }

        public String toString() {
            return String.valueOf(value);
        }

    }

    private static void ASSERT(boolean assertion) {
        if (!assertion)
            throw new AssertionError();
    }
}