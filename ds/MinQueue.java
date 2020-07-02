package library.ds;

import java.util.Comparator;

public class MinQueue<T> {

    public final Comparator<T> comparator;
    private MinStack<T> s1, s2;

    public MinQueue(Comparator<T> comparator) {
        this.comparator = comparator;
        s1 = new MinStack<>(comparator);
        s2 = new MinStack<>(comparator);
    }

    public void push(T item) {
        s1.push(item);
    }

    public T pop() {
        ensureItems();

        return s2.pop();
    }

    private void ensureItems() {
        if (s2.isEmpty()) {
            while (!s1.isEmpty()) {
                s2.push(s1.pop());
            }
        }
    }

    public T peek() {
        ensureItems();

        return s2.peek();
    }

    private T min(T x, T y) {
        if (comparator.compare(x, y) <= 0)
            return x;
        return y;
    }

    public boolean isEmpty() {
        return s1.isEmpty() && s2.isEmpty();
    }

    public T getMin() {
        if (s1.isEmpty())
            return s2.getMin();
        if (s2.isEmpty())
            return s1.getMin();
        return min(s1.getMin(), s2.getMin());
    }

    public T getMinOrDefault(T defaultValue) {
        return isEmpty() ? defaultValue : getMin();
    }

    public void clear() {
        s1.clear();
        s2.clear();
    }
}
