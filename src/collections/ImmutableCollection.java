package collections;

import java.util.Objects;

public final class ImmutableCollection<E> {
    private final E[] array;

    private ImmutableCollection(E[] array) {
        this.array = array.clone();
    }

    private ImmutableCollection() {
        this.array = (E[]) new Object[0];
    }


   public static <E> ImmutableCollection<E> of() {
        return new ImmutableCollection<>();
    }

    public static <E> ImmutableCollection<E> of(E... elements) {
        if (elements.length == 0) {
            return new ImmutableCollection<>();
        }
        for (E element : elements) {
            if (element == null) {
                throw new NullPointerException();
            }
        }

        return new ImmutableCollection<>(elements);
    }

    E[] getArray() {
        return array.clone();
    }

    public int size() {
        return array.length;
    }

    public boolean isEmpty() {
        return array.length == 0;
    }

    public boolean contains(E element) {
        for (E inner : array) {
            if (Objects.deepEquals(element, inner)) {
                return true;
            }
        }
        return false;
    }
}
