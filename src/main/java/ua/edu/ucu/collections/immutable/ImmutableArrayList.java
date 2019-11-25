package ua.edu.ucu.collections.immutable;

import java.util.Arrays;

public final class ImmutableArrayList implements ImmutableList {
    private Object[] array;

    public ImmutableArrayList() {
        array = new Object[0];
    }

    private void throwIndexExceptionAdd(int index) {
        if (index > array.length) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void throwIndexExceptionGet(int index) {
        if (index > array.length - 1) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public ImmutableArrayList add(Object e) {
        return addAll(size(), new Object[]{e});
    }

    @Override
    public ImmutableArrayList add(int index, Object e) {
        return addAll(index, new Object[]{e});
    }

    @Override
    public ImmutableArrayList addAll(Object[] c) {
        return addAll(size(), c);
    }

    @Override
    public ImmutableArrayList addAll(int index, Object[] c) {
        throwIndexExceptionAdd(index);
        ImmutableArrayList newArr = new ImmutableArrayList();
        newArr.array = Arrays.copyOf(array, array.length + c.length);
        for (int i = 0; i < c.length; i++) {
            newArr.array[index + i] = c[i];
        }
        int leftIndex = index;
        for (int j = index + c.length; j < newArr.array.length; j++) {
            newArr.array[j] = array[leftIndex];
            leftIndex++;
        }
        return newArr;
    }

    @Override
    public Object get(int index) {
        throwIndexExceptionGet(index);
        return array[index];
    }

    @Override
    public ImmutableArrayList remove(int index) {
        throwIndexExceptionGet(index);
        ImmutableArrayList newArr = new ImmutableArrayList();
        newArr.array = new Object[array.length - 1];
        for (int i = 0; i < index; i++) {
            newArr.array[i] = array[i];
        }
        for (int j = index + 1; j < array.length; j++) {
            newArr.array[j - 1] = array[j];
        }
        return newArr;
    }

    @Override
    public ImmutableArrayList set(int index, Object e) {
        throwIndexExceptionGet(index);
        ImmutableArrayList newArr = new ImmutableArrayList();
        newArr.array = Arrays.copyOf(array, array.length);
        newArr.array[index] = e;
        return newArr;
    }

    @Override
    public int indexOf(Object e) {
        int value = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(e)) {
                value = i;
                break;
            }
        }
        return value;
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public ImmutableArrayList clear() {
        return new ImmutableArrayList();
    }

    @Override
    public boolean isEmpty() {
        return array.length == 0;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, array.length);
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }
}

