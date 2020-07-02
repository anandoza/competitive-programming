package library.ds;

public class IntMinMaxStack {

    public IntStack items;
    public IntStack mins;
    public IntStack maxs;

    public IntMinMaxStack() {
        items = new IntStack();
        mins = new IntStack();
        maxs = new IntStack();
    }

    public IntMinMaxStack(int capacity) {
        items = new IntStack(capacity);
        mins = new IntStack(capacity);
        maxs = new IntStack(capacity);
    }

    public void push(int item) {
        int min, max;
        if (isEmpty()) {
            min = item;
            max = item;
        } else {
            min = Math.min(item, getMin());
            max = Math.max(item, getMax());
        }
        items.push(item);
        mins.push(min);
        maxs.push(max);
    }

    public int pop() {
        mins.pop();
        maxs.pop();
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

    public int getMax() {
        return maxs.peek();
    }

    public int getMaxOrDefault(int defaultValue) {
        return isEmpty() ? defaultValue : getMax();
    }

    public int peek() {
        return items.peek();
    }

    public void clear() {
        items.clear();
        mins.clear();
        maxs.clear();
    }
}
