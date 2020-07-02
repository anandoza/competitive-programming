package library.util.pair;

import java.util.Arrays;

public class Pii implements Comparable<Pii> {
    public final int first;
    public final int second;

    public Pii(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public static Pii of(int first, int second) {
        return new Pii(first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pii pair = (Pii) o;
        return first == pair.first && second == pair.second;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[]{first, second});
    }

    public String format() {
        return first + " " + second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ')';
    }

    @Override
    public int compareTo(Pii o) {
        if (first != o.first)
            return Integer.compare(first, o.first);
        return Integer.compare(second, o.second);
    }
}
