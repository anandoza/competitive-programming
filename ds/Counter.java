package library.ds;

import java.util.HashMap;

public class Counter<T> {
    public HashMap<T, Integer> count = new HashMap<>();

    public Counter() {
    }

    public Counter(T[] elements) {
        for (T t : elements)
            incr(t);
    }

    public Counter(Iterable<T> elements) {
        for (T t : elements)
            incr(t);
    }

    public void incr(T key) {
        count.merge(key, 1, Integer::sum);
    }

    public void decr(T key) {
        count.compute(key, (k, v) -> v - 1 == 0 ? null : v - 1);
    }

    public void add(T key, int add) {
        count.compute(key, (k, v) -> v + add == 0 ? null : v + add);
    }

    public int get(T key) {
        return count.getOrDefault(key, 0);
    }

    public boolean contains(T key) {
        return count.containsKey(key);
    }

    public int distinct() {
        return count.size();
    }
}
