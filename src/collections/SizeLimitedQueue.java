package collections;

import java.util.*;

public class SizeLimitedQueue<E> {
    Deque<E> elements;
    int capacity;

    public SizeLimitedQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        } else {
            this.capacity = capacity;
        }

        this.elements = new ArrayDeque<>(capacity);
    }


    public void add(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (isAtFullCapacity()) {
            elements.removeFirst();
            elements.addLast(element);
        } else {
            elements.addLast(element);
        }
    }

    public void clear() {
        elements.clear();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public int maxSize() {
        return capacity;
    }

    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return elements.peek();
    }

    public E remove() {
        var oldFirst = elements.getFirst();
        elements.removeFirst();
        return oldFirst;
    }

    public int size() {
        return elements.size();
    }

    public Object[] toArray() {
        Object[] array = new Object[size()];
        Deque<E> copy = new ArrayDeque<>(elements);
        for (int i = 0; i < array.length; i++) {
            array[i] = copy.removeFirst();
        }
        return array;
    }

    public E[] toArray(E[] e) {
        List<E> collectorArray = new ArrayList<>(size());
        collectorArray.addAll(elements);
        return collectorArray.toArray(e);
    }


    public boolean isAtFullCapacity() {
        return elements.size() == capacity;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        var iterator = elements.iterator();
        while (iterator.hasNext()) {
            var current = iterator.next();
            builder.append(current);
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
