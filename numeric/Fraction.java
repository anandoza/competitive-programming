package library.numeric;

import static library.numeric.NumberTheory.gcd;

public class Fraction implements Comparable<Fraction> {
    long n, d;

    public Fraction(long n, long d) {
        this.n = n;
        this.d = d;
        reduce();
    }

    public void reduce() {
        if (d < 0) {
            n *= -1;
            d *= -1;
        }
        long g = gcd(n, Math.abs(d));
        n /= g;
        d /= g;
    }

    public static Fraction of(long n, long d) {
        return new Fraction(n, d);
    }

    public Fraction copy() {
        return of(n, d);
    }

    public void mult(Fraction o) {
        n *= o.n;
        d *= o.d;

        reduce();
    }

    public void add(Fraction o) {
        long lcm = NumberTheory.lcm(d, o.d);
        n = n * (lcm / d) + o.n * (lcm / o.d);
        d = lcm;

        reduce();
    }

    @Override
    public String toString() {
        return n + " " + d;
    }

    @Override
    public int compareTo(Fraction o) {
        return Double.compare(1.0 * n / d, 1.0 * o.n / o.d);
    }

    public static void main(String[] args) {
        Fraction f = Fraction.of(10, 0);
        Fraction g = Fraction.of(-10, 0);
        System.out.println(f.compareTo(g));
    }
}
