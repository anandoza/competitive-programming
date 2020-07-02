package library.util.pair;

import java.util.Objects;

public class MPair<F, S> {
    public F first;
    public S second;

    public MPair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <F, S> MPair<F, S> of(F first, S second) {
        return new MPair<>(first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MPair<?, ?> pair = (MPair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ')';
    }
}
