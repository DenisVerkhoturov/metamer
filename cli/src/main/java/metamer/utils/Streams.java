package metamer.utils;

import java.util.List;
import java.util.stream.Stream;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.SIZED;
import static java.util.Spliterator.SORTED;
import static java.util.Spliterator.SUBSIZED;

public class Streams {
    public static <T> Stream<List<T>> chunks(final Splitter<T> splitter, final Stream<T> source) {
        final Spliterator<T> sourceSpliterator = source.spliterator();
        final Iterator<T> sourceIterator = Spliterators.iterator(sourceSpliterator);
        final Iterator<List<T>> iterator = splitter.apply(sourceIterator);
        final int characteristics = sourceSpliterator.characteristics() & ~(SIZED | SUBSIZED | SORTED);
        final Spliterator<List<T>> spliterator = Spliterators.spliterator(iterator, -1, characteristics);
        return StreamSupport.stream(spliterator, false);
    }
}

