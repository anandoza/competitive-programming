package library.util.pair;

import java.util.Arrays;

public class Pll implements Comparable<Pll> {
    public final long first;
    public final long second;

    public Pll(long first, long second) {
        this.first = first;
        this.second = second;
    }

    public static Pll of(long first, long second) {
        return new Pll(first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pll pair = (Pll) o;
        return first == pair.first && second == pair.second;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new long[]{first, second});
    }

    public String format() {
        return first + " " + second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ')';
    }

    @Override
    public int compareTo(Pll o) {
        if (first != o.first)
            return Long.compare(first, o.first);
        return Long.compare(second, o.second);
    }
}
