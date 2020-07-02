package library;

import library.util.Util;

import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;

public class BinarySearch {

    public static long binarySearchLong(LongPredicate bigEnough) {
        long tooSmall = 0, tooBig;
        if (bigEnough.test(tooSmall)) {
            return tooSmall;
        }

        for (tooBig = 1; !bigEnough.test(tooBig); tooBig *= 2)
            ;

        if (!bigEnough.test(tooBig - 1)) {
            return tooBig;
        }

        while (tooBig - tooSmall > 1) {
            long center = tooSmall + (tooBig - tooSmall) / 2;
            if (!bigEnough.test(center)) {
                tooSmall = center;
            } else {
                tooBig = center;
            }
        }

        return tooBig;
    }

    public static int binarySearch(IntPredicate bigEnough) {
        int tooSmall = 0, tooBig;
        if (bigEnough.test(tooSmall)) {
            return tooSmall;
        }

        for (tooBig = 1; !bigEnough.test(tooBig); tooBig *= 2)
            ;

        if (!bigEnough.test(tooBig - 1)) {
            return tooBig;
        }

        while (tooBig - tooSmall > 1) {
            int center = tooSmall + (tooBig - tooSmall) / 2;
            if (!bigEnough.test(center)) {
                tooSmall = center;
            } else {
                tooBig = center;
            }
        }

        return tooBig;
    }

    public static int binarySearch(IntPredicate bigEnough, int passingValue) {
        int tooSmall = 0, tooBig = passingValue;
        if (bigEnough.test(tooSmall)) {
            return tooSmall;
        }

        if (!bigEnough.test(tooBig - 1)) {
            return tooBig;
        }

        while (tooBig - tooSmall > 1) {
            int center = tooSmall + (tooBig - tooSmall) / 2;
            if (!bigEnough.test(center)) {
                tooSmall = center;
            } else {
                tooBig = center;
            }
        }

        return tooBig;
    }

    public static long binarySearchLong(LongPredicate bigEnough, long passingValue) {
        long tooSmall = 0, tooBig = passingValue;
        if (bigEnough.test(tooSmall)) {
            return tooSmall;
        }

        if (!bigEnough.test(tooBig - 1)) {
            return tooBig;
        }

        while (tooBig - tooSmall > 1) {
            long center = tooSmall + (tooBig - tooSmall) / 2;
            if (!bigEnough.test(center)) {
                tooSmall = center;
            } else {
                tooBig = center;
            }
        }

        return tooBig;
    }

    public static double binarySearchDouble(DoublePredicate bigEnough, double low, double high, double tolerance) {
        double tooSmall = low, tooBig = high;

        if (bigEnough.test(tooSmall)) {
            return tooSmall;
        }
        Util.ASSERT(bigEnough.test(tooBig));

        while (tooBig - tooSmall > tolerance) {
            double center = tooSmall + (tooBig - tooSmall) / 2;
            if (!bigEnough.test(center)) {
                tooSmall = center;
            } else {
                tooBig = center;
            }
        }

        return (tooSmall + tooBig) / 2;
    }

    public static double binarySearchDoubleFixedIterations(DoublePredicate bigEnough, double low, double high, int iterations) {
        double tooSmall = low, tooBig = high;

        if (bigEnough.test(tooSmall)) {
            return tooSmall;
        }
        Util.ASSERT(bigEnough.test(tooBig));

        for (int i = 0; i < iterations; i++) {
            double center = tooSmall + (tooBig - tooSmall) / 2;
            if (!bigEnough.test(center)) {
                tooSmall = center;
            } else {
                tooBig = center;
            }
        }

        return (tooSmall + tooBig) / 2;
    }

    /**
     * Finds the minimum of f(x) and returns x.
     * <p>
     * left, right are both inclusive
     */
    public static long ternarySearchLong(LongUnaryOperator f, long left, long right) {
        while (true) {
            long t = (right - left) / 3;
            if (t == 0) {
                long answer = left;
                long opt = f.applyAsLong(left);
                for (long x = left + 1; x <= right; x++) {
                    long candidate = f.applyAsLong(x);
                    if (candidate < opt) {
                        answer = x;
                        opt = candidate;
                    }
                }
                return answer;
            }
            long a = left + t;
            long b = right - t;
            if (f.applyAsLong(a) > f.applyAsLong(b)) {
                left = a;
            } else {
                right = b;
            }
        }
    }

    public static void main2(String[] args) {
        for (int i = 0; i <= 100; i++) {
            int finalI = i;
            LongPredicate atLeastI = x -> x >= finalI;
            if (binarySearchLong(atLeastI) != i) {
                System.out.println("wrong");
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 100; i++) {
            int finalI = i;
            DoublePredicate isSqrt = x -> x * x >= finalI;
            System.out.println(Math.sqrt(i) + "\t" + binarySearchDouble(isSqrt, 0, 100, 1e-1));
        }
    }
}

