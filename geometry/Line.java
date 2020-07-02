package library.geometry;

import java.util.Objects;

import static library.numeric.NumberTheory.gcd;
import static library.util.Util.ASSERT;

public class Line {
    // ax+by+c=0 && gcd(a,b,c)=1
    // a >= 0 and if a == 0, then b > 0
    final long a, b, c;

    public Line(Point p1, Point p2) {
        long a = p1.y - p2.y;
        long b = -p1.x + p2.x;

        ASSERT(a != 0 || b != 0);
        long c = -(a * p1.x + b * p1.y);

        long g = gcd(c, gcd(a, b));
        a /= g;
        b /= g;
        c /= g;

        if (a < 0) {
            a *= -1;
            b *= -1;
            c *= -1;
        }
        if (a == 0 && b < 0) {
            b *= -1;
            c *= -1;
        }

        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Line(long a, long b, long c) {
        ASSERT(a != 0 || b != 0);

        long g = gcd(c, gcd(a, b));
        a /= g;
        b /= g;
        c /= g;

        if (a < 0) {
            a *= -1;
            b *= -1;
            c *= -1;
        }
        if (a == 0 && b < 0) {
            b *= -1;
            c *= -1;
        }

        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean contains(Point p) {
        return a * p.x + b * p.y + c == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Line l = (Line) o;
        return a == l.a && b == l.b && c == l.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    @Override
    public String toString() {
        return String.format("[%dx + %dy + %d]", a, b, c);
    }
}
