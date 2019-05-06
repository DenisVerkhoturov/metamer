package metamer.utils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public interface Splitter<T> extends Function<Iterator<T>, Iterator<List<T>>> {
    static <T> Splitter<T> splitBefore(final T delimiter) {
        return iterator -> new SplitBefore<>(iterator, symbol ->
                symbol.toString().startsWith(delimiter.toString()));
    }
}
