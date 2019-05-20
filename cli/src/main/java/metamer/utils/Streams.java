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

