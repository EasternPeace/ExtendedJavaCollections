package collections;

import java.util.*;

public class Multiset<E> {

    Set<MultiElement<E>> elements;

    public Multiset() {
        this.elements = new LinkedHashSet<>();
    }

    public void add(E element) {
        MultiElement<E> found = find(element);
        if (found != null) {
            found.increment();
        } else {
            elements.add(new MultiElement<>(element));
        }
    }

    public void add(E element, int occurrences) {
        MultiElement<E> found = find(element);
        if (occurrences > 0) {
            if (found != null) {
                found.increase(occurrences);
            } else {
                elements.add(new MultiElement<>(element, occurrences));
            }
        } else {
            if (found != null) {
                if (Math.abs(occurrences) >= found.occurrence) {
                    elements.remove(found);
                } else {
                    found.increase(occurrences);
                }
            }
        }
    }

    public boolean contains(E element) {
        for (MultiElement<E> current : elements) {
            if (Objects.equals(current.value, element)) {
                return true;
            }
        }
        return false;
    }

    public int count(E element) {
        MultiElement<E> found = find(element);
        if (found != null) {
            return found.occurrence;
        } else {
            return 0;
        }
    }

    public Set<E> elementSet() {
        Set<E> elements =  new LinkedHashSet<>();
        for (MultiElement<E> multiElement : this.elements) {
            if (multiElement.occurrence > 0) {
                elements.add(multiElement.value);
            }
        }
        return elements;
    }

    public void remove(E element) {
        MultiElement<E> found = find(element);
        if (found != null) {
            found.decrement();
            if (found.occurrence <= 0) {
                elements.remove(found);
            }
        }
    }

    public void remove(E element, int occurrences) {
        MultiElement<E> found = find(element);
        if (found != null) {
            found.decrease(occurrences);
            if (found.occurrence <= 0) {
                elements.remove(found);
            }
        }
    }

    public void setCount(E element, int count) {
        MultiElement<E> found = find(element);
        if (found != null) {
            if (count == 0) {
                elements.remove(found);
            } else {
                found.setOccurrence(count);
            }
        }
    }

    public void setCount(E element, int oldCount, int newCount) {
        MultiElement<E> found = find(element);
        if (found != null) {
            if (found.occurrence == oldCount) {
                if (newCount == 0) {
                    elements.remove(found);
                } else {
                    setCount(element, newCount);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        Iterator<MultiElement<E>> iterator = elements.iterator();
        while (iterator.hasNext()) {
            var current = iterator.next();
            for (int i = 0; i < current.occurrence; i++) {
                builder.append(current.value);
                if (i < current.occurrence - 1) {
                    builder.append(", ");
                }
            }
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private MultiElement<E> find(E value) {
        for (MultiElement<E> element : elements) {
            if (Objects.equals(element.value, value)) {
                return element;
            }
        }
        return null;
    }


    static class MultiElement<E> {
        private final E value;
        private int occurrence;

        MultiElement(E element) {
            this.value = element;
            occurrence = 1;
        }

        MultiElement(E element, int occurrence) {
            if (occurrence <= 0) {
                throw new IllegalArgumentException();
            }
            this.value = element;
            this.occurrence = occurrence;
        }

        void setOccurrence(int occurrence) {
            if (occurrence < 0) {
                return;
            }
            this.occurrence = occurrence;
        }

        void increment() {
            occurrence++;
        }

        void decrement() {
            if (occurrence > 0) {
                occurrence--;
            }
        }

        void increase(int occurrences) {
            if (occurrences < 0) {
                if (Math.abs(occurrences) > this.occurrence) {
                    this.occurrence = 0;
                }
            } else {
                this.occurrence += occurrences;
            }
        }

        void decrease(int occurrences) {
            if (occurrences < 0) {
                return;
            }
            if (occurrences >= this.occurrence) {
                this.occurrence = 0;
            } else {
                occurrence -= occurrences;
            }
        }

        public E getValue() {
            return value;
        }

        public int getOccurrence() {
            return occurrence;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MultiElement<?> that = (MultiElement<?>) o;
            return Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}
