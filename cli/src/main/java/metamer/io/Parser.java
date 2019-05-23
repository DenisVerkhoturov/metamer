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
package metamer.io;

import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.List;
import java.util.stream.Stream;

/**
 * Interface for input-output workflow.
 *
 * @param <T> Type of Record: {@link metamer.fasta.Record} or {@link metamer.fastq.Record}.
 */
public interface Parser<T> {
    /**
     * Represent record in correct form for writing to output source.
     *
     * @param record Current record with fields.
     * @return Stream of strings
     */
    Stream<String> show(T record);

    /**
     * Represent stream of records in correct form for writing to output source.
     *
     * @param records Stream of records prepared for converting into strings.
     * @return Stream of strings prepared for writing.
     */
    Stream<String> show(Stream<T> records);

    /**
     * Converting a piece of information for one record from input source.
     *
     * @param lines Strings read from input source.
     * @return Exception if there were problems or correct Record.
     */
    Either<Exception, T> read(List<String> lines);

    /**
     * Converting all information into Records.
     *
     * @param lines Stream of strings read from input source.
     * @return Exception if there were problems or sequence of correct Records.
     */
    Either<Exception, Seq<T>> read(Stream<String> lines);
}
