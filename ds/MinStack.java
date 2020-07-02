package library.ds;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

public class MinStack<T> {

    public final Comparator<T> comparator;
    Deque<T> items = new ArrayDeque<>();
    Deque<T> mins = new ArrayDeque<>();

    public MinStack(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public void push(T item) {
        T min;
        if (isEmpty()) {
            min = item;
        } else {
            min = min(item, getMin());
        }
        items.addLast(item);
        mins.addLast(min);
    }

    public T pop() {
        mins.pollLast();
        return items.pollLast();
    }

    private T min(T x, T y) {
        if (comparator.compare(x, y) <= 0)
            return x;
        return y;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public T getMin() {
        return mins.peekLast();
    }

    public T getMinOrDefault(T defaultValue) {
        return isEmpty() ? defaultValue : getMin();
    }

    public T peek() {
        return items.peekLast();
    }

    public void clear() {
        items.clear();
        mins.clear();
    }
}
