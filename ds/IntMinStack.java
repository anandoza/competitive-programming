package library.ds;

public class IntMinStack {

    IntStack items;
    IntStack mins;

    public IntMinStack() {
        items = new IntStack();
        mins = new IntStack();
    }

    public IntMinStack(int capacity) {
        items = new IntStack(capacity);
        mins = new IntStack(capacity);
    }

    public void push(int item) {
        int min;
        if (isEmpty()) {
            min = item;
        } else {
            min = Math.min(item, getMin());
        }
        items.push(item);
        mins.push(min);
    }

    public int pop() {
        mins.pop();
        return items.pop();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getMin() {
        return mins.peek();
    }

    public int getMinOrDefault(int defaultValue) {
        return isEmpty() ? defaultValue : getMin();
    }

    public int peek() {
        return items.peek();
    }

    public void clear() {
        items.clear();
        mins.clear();
    }
}
