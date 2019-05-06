package metamer.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class SplitBefore<T> implements Iterator<List<T>> {
    private final Iterator<T> downstream;
    private final Predicate<T> delimiter;
    private T iteratorValue;

    SplitBefore(final Iterator<T> downstream, final Predicate<T> delimiter) {
        this.downstream = downstream;
        this.delimiter = delimiter;
    }

    @Override
    public boolean hasNext() {
        return downstream.hasNext() || iteratorValue != null;
    }

    @Override
    public List<T> next() {
        if (!hasNext()) throw new NoSuchElementException();

        final List<T> list = new ArrayList<>();
        if (iteratorValue != null) {
            list.add(iteratorValue);
            iteratorValue = null;
        }

        while (downstream.hasNext()) {
            iteratorValue = downstream.next();
            if (delimiter.test(iteratorValue)) break;
            list.add(iteratorValue);
            iteratorValue = null;
        }

        return list.isEmpty() ? next() : list;
    }
}
