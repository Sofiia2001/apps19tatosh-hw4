package ua.edu.ucu.collections;

import ua.edu.ucu.collections.immutable.ImmutableArrayList;

public class Queue {
    private ImmutableArrayList array;

    public Queue() {
        array = new ImmutableArrayList();
    }

    Object peek() {
        return array.get(0);
    }

    public Object dequeue() {
        Object first = peek();
        array = array.remove(0);
        return first;
    }

    public void enqueue(Object e) {
        array = array.add(array.size(), e);
    }

    public boolean isEmpty() {
        return array.isEmpty();
    }

    public Object[] toArray() {
        return array.toArray();
    }
}
