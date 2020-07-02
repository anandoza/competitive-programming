package library.numeric;

import java.util.BitSet;

public class Primes {

    public final BitSet isPrime;
    public final int[] primes;

    /**
     * Generates isPrime from 1 to N, inclusive.
     */
    public Primes(int N) {
        if (N <= 1) {
            isPrime = new BitSet();
            primes = new int[0];
            return;
        }
        isPrime = new BitSet(N);
        isPrime.set(2, N + 1);
        for (int p = isPrime.nextSetBit(0); p * p <= N; p = isPrime.nextSetBit(p + 1)) {
            for (int composite = p * 2; composite <= N; composite += p) {
                isPrime.clear(composite);
            }
        }

        primes = new int[isPrime.cardinality()];
        int p = isPrime.nextSetBit(0);
        for (int i = 0; i < primes.length; i++) {
            primes[i] = p;
            p = isPrime.nextSetBit(p + 1);
        }
    }

    public boolean isPrime(int p) {
        return isPrime.get(p);
    }

    public static void main(String[] args) {
        long t = System.nanoTime();
        Primes primes = new Primes(1000_000_000);
        System.out.println(primes.primes.length);
        System.out.format("%.6f ms%n", ((double) (System.nanoTime() - t)) / 1_000_000);
    }

}