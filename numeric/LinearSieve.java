package library.numeric;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static library.numeric.NumberTheory.pow;
import static library.numeric.NumberTheory.powInt;

public class LinearSieve {

    public final BitSet isComposite;
    public final int[] leastPrimeDivisor;
    public final int[] primes;

    /**
     * Generates primes from 1 to n, inclusive.
     */
    public LinearSieve(int n) {
        double estimate;
        if (n < 10000) {
            estimate = n + 1;
        } else {
            estimate = ((double) n) / Math.log(n);
            estimate *= 1.1;
        }
        int[] primes = new int[(int) estimate];
        leastPrimeDivisor = new int[n + 1];
        int size = 0;

        if (n <= 1) {
            isComposite = new BitSet();
        } else {
            isComposite = new BitSet(n + 1);

            for (int i = 2; i <= n; i++) {
                if (!isComposite.get(i)) {
                    if (size >= primes.length)
                        primes = resize(primes);
                    primes[size++] = i;
                    leastPrimeDivisor[i] = i;
                }

                for (int j = 0; j < size; j++) {
                    int p = primes[j];
                    if (i * p > n)
                        break;
                    isComposite.set(i * p);
                    leastPrimeDivisor[i * p] = p;
                    if (i % p == 0)
                        break;
                }
            }
        }

        this.primes = new int[size];
        System.arraycopy(primes, 0, this.primes, 0, size);
    }

    private static int[] resize(int[] primes) {
        int newSize = 1 + ((primes.length * 11) / 10);
        int[] newPrimes = new int[newSize];
        System.arraycopy(primes, 0, newPrimes, 0, primes.length);
        return newPrimes;
    }

    interface PrimePowerFunction {
        /**
         * This should be f(p^k) where p is prime.
         */
        int forPrimePower(int p, int k);
    }

    public static final PrimePowerFunction constant = (p, k) -> 1;
    public static final PrimePowerFunction unit = (p, k) -> (k == 0 ? 1 : 0);
    public static final PrimePowerFunction divisorCount = (p, k) -> k + 1;
    public static final PrimePowerFunction divisorSum = (p, k) -> (int) ((pow(p, k + 1) - 1) / (p - 1));
    public static final PrimePowerFunction mobius = (p, k) -> (k == 0 ? 1 : 0) - (k == 1 ? 1 : 0);
    public static final PrimePowerFunction totient = (p, k) -> k == 0 ? 1 : powInt(p, k - 1) * (p - 1);

    public static int[] sieve(int n, PrimePowerFunction function) {
        ASSERT(n >= 1);

        BitSet isComposite = new BitSet();
        int[] f = new int[n + 1];
        int[] count = new int[n + 1];
        List<Integer> primes = new ArrayList<>();

        f[1] = 1;

        for (int i = 2; i <= n; i++) {
            if (!isComposite.get(i)) {
                primes.add(i);
                f[i] = function.forPrimePower(i, 1);
                count[i] = 1;
            }

            for (int p : primes) {
                if (i * p > n)
                    break;

                isComposite.set(i * p);

                if (i % p == 0) {
                    f[i * p] = f[i] * function.forPrimePower(p, count[i] + 1);
                    int div = function.forPrimePower(p, count[i]);
                    if (div != 0)
                        f[i * p] /= div;
                    else
                        ASSERT(f[i * p] == 0);
                    count[i * p] = count[i] + 1;
                } else {
                    f[i * p] = f[i] * f[p];
                    count[i * p] = 1;
                }
            }
        }

        return f;
    }

    public boolean isPrime(int p) {
        return !isComposite.get(p);
    }

    public static void main(String[] args) {
        for (int n : new int[]{10000, 100_000, 1000_000, 10_000_000, 100_000_000, 1_000_000_000}) {
            System.out.format("n = %d (10^%d)%n", n, String.valueOf(n).length() - 1);
            long t = System.nanoTime();

            Primes primes1 = new Primes(n);
            System.out.format("Primes: %.6f ms%n", ((double) (System.nanoTime() - t)) / 1_000_000);

            t = System.nanoTime();

            LinearSieve primes = new LinearSieve(n);
            System.out.format("Linear: %.6f ms%n", ((double) (System.nanoTime() - t)) / 1_000_000);

            System.out.format("(# of primes = %d)%n", primes.primes.length);
            ASSERT(primes.primes.length == primes1.primes.length);

            System.out.println();
        }

        //
        //        int[] f = sieve(100, mobius);
        //        System.out.println(Arrays.toString(f));
        //        for (int i = 1; i <= 30; i++) {
        //            System.out.format("%4d: %d%n", i, f[i]);
        //        }

        //        int[] f = sieve(100, divisorSum);
        //        totient = sieve(100, (p, k) -> k + 1);
        //        for (int i = 1; i <= 20; i++) {
        //            System.out.format("%4d: %d%n", i, f[i]);
        //        }

    }

    private static void ASSERT(boolean assertion) {
        if (!assertion)
            throw new AssertionError();
    }
}