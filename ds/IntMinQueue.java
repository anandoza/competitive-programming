package library.ds;

public class IntMinQueue {

    private IntMinStack s1, s2;

    public IntMinQueue() {
        s1 = new IntMinStack();
        s2 = new IntMinStack();
    }

    public IntMinQueue(int capacity) {
        s1 = new IntMinStack(capacity);
        s2 = new IntMinStack(capacity);
    }

    public void push(int item) {
        s1.push(item);
    }

    public int pop() {
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

    public int peek() {
        ensureItems();

        return s2.peek();
    }

    public boolean isEmpty() {
        return s1.isEmpty() && s2.isEmpty();
    }

    public int getMin() {
        if (s1.isEmpty())
            return s2.getMin();
        if (s2.isEmpty())
            return s1.getMin();
        return Math.min(s1.getMin(), s2.getMin());
    }

    public int getMinOrDefault(int defaultValue) {
        return isEmpty() ? defaultValue : getMin();
    }

    public void clear() {
        s1.clear();
        s2.clear();
    }
}
