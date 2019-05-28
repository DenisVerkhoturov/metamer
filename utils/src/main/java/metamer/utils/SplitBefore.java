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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 *A namespace for utility functions for "Lazy parsing" strategy.
 *
 * @param <T> Type of Record: {@link metamer.fasta.Record} or {@link metamer.fastq.Record}.
 */
public class SplitBefore<T> implements Iterator<List<T>> {
    private final Iterator<T> downstream;
    private final Predicate<T> delimiter;
    private T iteratorValue;

    /**
     * Costructor - initializing fields.
     * @param downstream    Next iterator from stream.
     * @param delimiter     Type of delimiter between records (> or @).
     */
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
