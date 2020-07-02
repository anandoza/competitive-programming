package library.ds;

import java.util.Arrays;

public class IntStack {

    int[] array;
    int size = 0;

    public IntStack() {
        this(10);
    }

    public IntStack(int capacity) {
        array = new int[capacity];
    }

    public void push(int item) {
        if (size >= array.length)
            array = resize(array);
        array[size++] = item;
    }

    public int pop() {
        return array[--size];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int peek() {
        return array[size - 1];
    }

    public void clear() {
        size = 0;
    }

    private static int[] resize(int[] array) {
        int newSize = 1 + ((array.length * 16) / 10);
        int[] newArray = new int[newSize];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    public static void main(String[] args) {
        IntStack s = new IntStack();
        System.out.println(s.isEmpty());
    }

    @Override
    public String toString() {
        int[] trimmed = new int[size];
        System.arraycopy(array, 0, trimmed, 0, size);
        return Arrays.toString(trimmed);
    }
}
