package metamer.utils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Splitter<T> extends Function<Iterator<T>, Iterator<List<T>>> {
    static <T> Splitter<T> splitBefore(final Predicate<T> delimiter) {
        return iterator -> new SplitBefore<>(iterator, delimiter);
    }
}
