package library.geometry;

import library.InputReader;
import library.numeric.NumberTheory;
import library.util.Util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import static library.geometry.Classification.*;

public class Point implements Comparable<Point> {
    public long x;
    public long y;

    public Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public static Point of(long x, long y) {
        return new Point(x, y);
    }

    public static Point read(InputReader in) {
        return new Point(in.nextLong(), in.nextLong());
    }

    public static Point[] readPoints(InputReader in, int n) {
        Point[] p = new Point[n];
        for (int i = 0; i < n; i++) {
            p[i] = read(in);
        }
        return p;
    }

    public static Point zero() {
        return new Point(0, 0);
    }

    public Point copy() {
        return new Point(x, y);
    }

    public long norm() {
        return x * x + y * y;
    }

    public double len() {
        return Math.sqrt(norm());
    }

    public double ang() {
        return Math.atan2(y, x);
    }

    public Point reduceAng() {
        long x = this.x;
        long y = this.y;
        if (y < 0) {
            x *= -1;
            y *= -1;
        }
        if (y == 0 && x < 0) {
            x *= -1;
        }
        long g = NumberTheory.gcd(x, y);
        x /= g;
        y /= g;
        return new Point(x, y);
    }

    public Point add(Point o) {
        x += o.x;
        y += o.y;
        return this;
    }

    public Point subtract(Point o) {
        x -= o.x;
        y -= o.y;
        return this;
    }

    public Point mult(long c) {
        x *= c;
        y *= c;
        return this;
    }

    public Point div(long c) {
        x /= c;
        y /= c;
        return this;
    }

    public static Point add(Point a, Point b) {
        return a.copy().add(b);
    }

    public static Point subtract(Point a, Point b) {
        return a.copy().subtract(b);
    }

    public static Point mult(Point a, long c) {
        return a.copy().mult(c);
    }

    public static Point div(Point a, long c) {
        return a.copy().div(c);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public static long dot(Point a, Point b) {
        return a.x * b.x + a.y * b.y;
    }

    public static long cross(Point a, Point b) {
        return a.x * b.y - a.y * b.x;
    }

    public static long cross(Point p, Point a, Point b) {
        return cross(subtract(a, p), subtract(b, p));
    }

    @Override
    public int compareTo(Point o) {
        int ret = Long.compare(x, o.x);
        if (ret != 0)
            return ret;
        return Long.compare(y, o.y);
    }

    public Classification classify(Point[] polygon) {
        boolean ans = false;
        for (int i = 0; i < polygon.length; i++) {
            Point a = polygon[i], b = polygon[(i + 1) % polygon.length];
            if (cross(a, b, this) == 0 && (a.compareTo(this) <= 0 || b.compareTo(this) <= 0) && (this.compareTo(a) <= 0 || this.compareTo(b) <= 0))
                return ON;
            if (a.y > b.y) {
                Point t = a;
                a = b;
                b = t;
            }
            if (a.y <= y && y < b.y && cross(this, a, b) > 0)
                ans ^= true;
        }
        return ans ? IN : OUT;
    }

    /**
     * points must be distinct
     */
    public static boolean collinear(Point... points) {
        if (points.length <= 2)
            return true;

        Point a = points[0], b = points[1];
        for (int i = 2; i < points.length; i++) {
            if (cross(a, b, points[i]) != 0)
                return false;
        }
        return true;
    }

    public static void sortByAngle(Point[] points) {
        int firstNonZero = Util.partition(points, p -> p.x == 0 && p.y == 0);
        int pivot = Util.partition(points, p -> p.y > 0 || p.y == 0 && p.x > 0, firstNonZero, points.length);
        Arrays.sort(points, firstNonZero, pivot, SORT_BY_ANGLE_IN_SAME_HALF_PLANE);
        Arrays.sort(points, pivot, points.length, SORT_BY_ANGLE_IN_SAME_HALF_PLANE);
    }

    public static final Comparator<Point> SORT_BY_ANGLE_IN_SAME_HALF_PLANE = (a, b) -> {
        long r = Point.cross(b, a);
        return r > 0 ? 1 : r < 0 ? -1 : 0;
    };
}
