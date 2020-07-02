package library.geometry;

import java.util.Objects;

import static library.geometry.Classification.*;

public class PointF implements Comparable<PointF> {

    public double x, y;

    public PointF(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static PointF zero() {
        return new PointF(0, 0);
    }

    public PointF copy() {
        return new PointF(x, y);
    }

    public double norm() {
        return x * x + y * y;
    }

    public double len() {
        return Math.sqrt(norm());
    }

    public double ang() {
        return Math.atan2(y, x);
    }

    public PointF add(PointF o) {
        x += o.x;
        y += o.y;
        return this;
    }

    public PointF subtract(PointF o) {
        x -= o.x;
        y -= o.y;
        return this;
    }

    public PointF mult(double c) {
        x *= c;
        y *= c;
        return this;
    }

    public PointF mult(PointF o) {
        double newX = x * o.x - y * o.y;
        double newY = x * o.y + y * o.x;
        x = newX;
        y = newY;
        return this;
    }

    public PointF div(double c) {
        x /= c;
        y /= c;
        return this;
    }

    public static PointF add(PointF a, PointF b) {
        return a.copy().add(b);
    }

    public static PointF subtract(PointF a, PointF b) {
        return a.copy().subtract(b);
    }

    public static PointF mult(PointF a, double c) {
        return a.copy().mult(c);
    }

    public static PointF mult(PointF a, PointF b) {
        return a.copy().mult(b);
    }

    public static PointF div(PointF a, double c) {
        return a.copy().div(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PointF point = (PointF) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public static double dot(PointF a, PointF b) {
        return a.x * b.x + a.y * b.y;
    }

    public static double cross(PointF a, PointF b) {
        return a.x * b.y - a.y * b.x;
    }

    public static double cross(PointF p, PointF a, PointF b) {
        return cross(subtract(a, p), subtract(b, p));
    }

    @Override
    public int compareTo(PointF o) {
        int ret = Double.compare(x, o.x);
        if (ret != 0)
            return ret;
        return Double.compare(y, o.y);
    }

    public Classification classify(PointF[] polygon) {
        boolean ans = false;
        for (int i = 0; i < polygon.length; i++) {
            PointF a = polygon[i], b = polygon[(i + 1) % polygon.length];
            if (cross(a, b, this) == 0 && (a.compareTo(this) <= 0 || b.compareTo(this) <= 0) && (this.compareTo(a) <= 0 || this.compareTo(b) <= 0))
                return ON;
            if (a.y > b.y) {
                PointF t = a;
                a = b;
                b = t;
            }
            if (a.y <= y && y < b.y && cross(this, a, b) > 0)
                ans ^= true;
        }
        return ans ? IN : OUT;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    /**
     * points must be distinct
     */
    public static boolean collinear(PointF... points) {
        if (points.length <= 2)
            return true;

        PointF a = points[0], b = points[1];
        for (int i = 2; i < points.length; i++) {
            if (cross(a, b, points[i]) != 0)
                return false;
        }
        return true;
    }
}
