package collections.range;

import static java.util.Objects.requireNonNull;

public class Range<C extends Comparable> {
    private final C lowerBound;
    private final boolean isLowerBoundOpen;
    private final C upperBound;
    private final boolean isUpperBoundOpen;

    private Range(C lowerBound, boolean isLowerBoundOpen, C upperBound, boolean isUpperBoundOpen) {
        this.lowerBound = lowerBound;
        this.isLowerBoundOpen = isLowerBoundOpen;
        this.upperBound = upperBound;
        this.isUpperBoundOpen = isUpperBoundOpen;
    }

    public static <C extends Comparable<C>> Range<C> open(C a, C b) {
        requireNonNull(a, "Lower bound cannot be null");
        requireNonNull(b, "Upper bound cannot be null");
        if (a.compareTo(b) >= 0) {
            throw new IllegalArgumentException("Lower bound must be less than upper bound");
        }
        return new Range<>(a, true, b, true);
    }

    public static <C extends Comparable<C>> Range<C> closed(C a, C b) {
        requireNonNull(a, "Lower bound cannot be null");
        requireNonNull(b, "Upper bound cannot be null");
        if (a.compareTo(b) > 0) {
            throw new IllegalArgumentException("Lower bound must be less than or equal to upper bound");
        }
        return new Range<>(a, false, b, false);
    }

    public static <C extends Comparable<C>> Range<C> openClosed(C a, C b) {
        requireNonNull(a, "Lower bound cannot be null");
        requireNonNull(b, "Upper bound cannot be null");
        if (a.compareTo(b) > 0) {
            throw new IllegalArgumentException("Lower bound must be less than upper bound");
        }
        if (a.compareTo(b) == 0) {
            return empty();
        }
        return new Range<>(a, true, b, false);
    }

    public static <C extends Comparable<C>> Range<C> closedOpen(C a, C b) {
        requireNonNull(a, "Lower bound cannot be null");
        requireNonNull(b, "Upper bound cannot be null");

        if (a.compareTo(b) > 0) {
            throw new IllegalArgumentException("Lower bound must be less than upper bound");
        }
        if (a.compareTo(b) == 0) {
            return empty();
        }
        return new Range<>(a, false, b, true);
    }

    public static <C extends Comparable<C>> Range<C> greaterThan(C a) {
        requireNonNull(a, "Lower bound cannot be null");
        return new Range<>(a, true, null, true);
    }

    public static <C extends Comparable<C>> Range<C> atLeast(C a) {
        requireNonNull(a, "Lower bound cannot be null");
        return new Range<>(a, false, null, true);
    }

    public static <C extends Comparable<C>> Range<C> lessThan(C a) {
        requireNonNull(a, "Upper bound cannot be null");
        return new Range<>(null, true, a, true);
    }

    public static <C extends Comparable<C>> Range<C> atMost(C a) {
        requireNonNull(a, "Upper bound cannot be null");
        return new Range<>(null, true, a, false);
    }

    public boolean contains(C element) {
        requireNonNull(element, "Element cannot be null");

        if (this.isEmpty()) {
            return false;
        }

        boolean lowerBoundCheck = (lowerBound == null) || (isLowerBoundOpen ? lowerBound.compareTo(element) < 0 : lowerBound.compareTo(element) <= 0);
        boolean upperBoundCheck = (upperBound == null) || (isUpperBoundOpen ? upperBound.compareTo(element) > 0 : upperBound.compareTo(element) >= 0);

        return lowerBoundCheck && upperBoundCheck;
    }

    public boolean encloses(Range<C> other) {
        requireNonNull(other, "Other range cannot be null");

        if (this.isEmpty()) {
           return false;
        }

        if (other.isEmpty() && !this.isEmpty()) {
            return true;
        }

        boolean lowerBoundCheck = (this.lowerBound == null ||
                (other.lowerBound != null &&
                        (this.lowerBound.compareTo(other.lowerBound) < 0 ||
                                (this.lowerBound.compareTo(other.lowerBound) == 0 && (!this.isLowerBoundOpen || other.isLowerBoundOpen)))));

        boolean upperBoundCheck = (this.upperBound == null ||
                (other.upperBound != null &&
                        (this.upperBound.compareTo(other.upperBound) > 0 ||
                                (this.upperBound.compareTo(other.upperBound) == 0 && (!this.isUpperBoundOpen || other.isUpperBoundOpen)))));

        return lowerBoundCheck && upperBoundCheck;
    }

    public Range<C> span(Range<C> other) {
        requireNonNull(other, "Other range cannot be null");
        C newLowerBound;
        C newUpperBound;
        boolean isNewLowerBoundOpen;
        boolean isNewUpperBoundOpen;

        if (this.isEmpty()) {
            if (other.isEmpty()) {
                return this;
            } else {
                return other;
            }
         }

        if (other.isEmpty()) {
            if (this.isEmpty()) {
                return other;
            } else {
                return this;
            }
        }

        if (this.lowerBound == null || other.lowerBound == null) {
            newLowerBound = null;
            isNewLowerBoundOpen = true;
        } else {
            if (isLowerBoundOpen ? lowerBound.compareTo(other.lowerBound) < 0 : lowerBound.compareTo(other.lowerBound) <= 0) {
                newLowerBound = this.lowerBound;
                isNewLowerBoundOpen = this.isLowerBoundOpen;
            } else {
                newLowerBound = other.lowerBound;
                isNewLowerBoundOpen = other.isLowerBoundOpen;
            }
        }

        if (this.upperBound == null || other.upperBound == null) {
            newUpperBound = null;
            isNewUpperBoundOpen = true;
        } else {
            if ((isUpperBoundOpen ? upperBound.compareTo(other.upperBound) > 0 : upperBound.compareTo(other.upperBound) >= 0)) {
                newUpperBound = this.upperBound;
                isNewUpperBoundOpen = this.isUpperBoundOpen;
            } else {
                newUpperBound = other.upperBound;
                isNewUpperBoundOpen = other.isUpperBoundOpen;
            }
        }

        return new Range<>(newLowerBound, isNewLowerBoundOpen, newUpperBound, isNewUpperBoundOpen);
    }

    public Range<C> intersection(Range<C> other) {
        requireNonNull(other, "Other range cannot be null");
        C newLowerBound;
        C newUpperBound;
        boolean isNewLowerBoundOpen;
        boolean isNewUpperBoundOpen;

        if (this.isEmpty()) {
            return this;
        }

        if (other.isEmpty()) {
            return  other;
        }

        if ((lowerBound == null && upperBound == null) && (other.lowerBound == null && other.upperBound == null)) {
            return all();
        }

        // Determine the new lower bound
        if (lowerBound == null && other.lowerBound == null) {
            newLowerBound = null;
            isNewLowerBoundOpen = true;
        } else if (lowerBound == null) {
            newLowerBound = other.lowerBound;
            isNewLowerBoundOpen = other.isLowerBoundOpen;
        } else if (other.lowerBound == null) {
            newLowerBound = lowerBound;
            isNewLowerBoundOpen = isLowerBoundOpen;
        } else {
            int compareLower = lowerBound.compareTo(other.lowerBound);
            if (compareLower > 0 || (compareLower == 0 && isLowerBoundOpen && !other.isLowerBoundOpen)) {
                newLowerBound = lowerBound;
                isNewLowerBoundOpen = isLowerBoundOpen;
            } else {
                newLowerBound = other.lowerBound;
                isNewLowerBoundOpen = other.isLowerBoundOpen;
            }
        }

        // Determine the new upper bound
        if (upperBound == null && other.upperBound == null) {
            newUpperBound = null;
            isNewUpperBoundOpen = true;
        } else if (upperBound == null) {
            newUpperBound = other.upperBound;
            isNewUpperBoundOpen = other.isUpperBoundOpen;
        } else if (other.upperBound == null) {
            newUpperBound = upperBound;
            isNewUpperBoundOpen = isUpperBoundOpen;
        } else {
            int compareUpper = upperBound.compareTo(other.upperBound);
            if (compareUpper < 0 || (compareUpper == 0 && isUpperBoundOpen && !other.isUpperBoundOpen)) {
                newUpperBound = upperBound;
                isNewUpperBoundOpen = isUpperBoundOpen;
            } else {
                newUpperBound = other.upperBound;
                isNewUpperBoundOpen = other.isUpperBoundOpen;
            }
        }

        // Check if bounds are inverted or equal with open bounds
        if (newLowerBound != null && newUpperBound != null) {
            int compareBounds = newLowerBound.compareTo(newUpperBound);
            if (compareBounds > 0 || (compareBounds == 0 && (isNewLowerBoundOpen || isNewUpperBoundOpen))) {
                return empty();
            }
        }

        return new Range<>(newLowerBound, isNewLowerBoundOpen, newUpperBound, isNewUpperBoundOpen);
    }


    public boolean isEmpty() {
        return (this.lowerBound == null && this.upperBound == null && !this.isLowerBoundOpen && !this.isUpperBoundOpen);
    }

    public static <C extends Comparable<C>> Range<C> empty() {
        return new Range<>(null, false, null, false);
    }

    public static <C extends Comparable<C>> Range<C> all() {
        return new Range<>(null, true, null, true);
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "EMPTY";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(isLowerBoundOpen ? "(" : "[");
        builder.append(lowerBound == null ? "-INF" : lowerBound);
        builder.append(", ");
        builder.append(upperBound == null ? "INF" : upperBound);
        builder.append(isUpperBoundOpen ? ")" : "]");
        return builder.toString();
    }
}
