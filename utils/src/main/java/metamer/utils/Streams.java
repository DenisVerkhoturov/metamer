/*
 * MIT License
 *
 * Copyright (c) 2019-present Denis Verkhoturov, Aleksandra Klimina,
 * Sophia Shalgueva, Irina Shapovalova, Anna Brusnitsyna
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

/**
 * A namespace for utility functions that work with {@link Stream streams}.
 */
public class Streams {

    /**
     * Split stream into chunks.
     *
     * Lazily and sequentially group provided stream into chunks using a {@link Splitter splitter} as a strategy.
     *
     * Example:
     * {@code
     * final Stream<Character> symbols = Stream.of('|', 'a', '|', 'b', 'c', '|');
     * final Stream<List<Character>> chunks = chunks(splitBefore(symbol -> symbol.equals('|')), symbols);
     * // Stream.of(List.of('|', 'a'), List.of('|', 'b', 'c'), List.of('|'))
     * }
     *
     * @param splitter Strategy that defines the way provided {@code source} {@link Stream} will be split.
     * @param source   Stream of elements that will be grouped.
     * @param <T>      Grouping functions makes no difference about what to work with and only
     * @return Stream of chunks provided that {@code source} {@link Stream} is not empty,
     *         otherwise, an empty {@link Stream}.
     */
    public static <T> Stream<List<T>> chunks(final Splitter<T> splitter, final Stream<T> source) {
        final Spliterator<T> sourceSpliterator = source.spliterator();
        final Iterator<T> sourceIterator = Spliterators.iterator(sourceSpliterator);
        final Iterator<List<T>> iterator = splitter.apply(sourceIterator);
        final int characteristics = sourceSpliterator.characteristics() & ~(SIZED | SUBSIZED | SORTED);
        final Spliterator<List<T>> spliterator = Spliterators.spliterator(iterator, -1, characteristics);
        return StreamSupport.stream(spliterator, false);
    }
}

