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

import io.vavr.Value;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;

/**
 * Class for reading from stdin.
 *
 * @param <T> Type of Record: {@link metamer.fasta.Record} or {@link metamer.fastq.Record}.
 */
public class StdInReader<T> implements Reader {
    private String id;
    private Parser<T> parser;

    /**
     * Constructor - initializing all fields.
     *
     * @param parser Type of parser for correct choice for record type.
     */
    public StdInReader(final Parser<T> parser) {
        this.id = System.in.toString();
        this.parser = parser;
    }

    /**
     * Function for reading from stdin.
     *
     * @return Stream of records for further work.
     */
    public Stream<T> read() {
        try (final Stream<String> lines = new BufferedReader(new InputStreamReader(System.in)).lines()) {
            final Either<Exception, Seq<T>> value = this.parser().read(lines);
            return value.map(Value::toJavaStream).getOrElse(Stream.empty());
        }
    }

    /**
     * Get current parser.
     *
     * Parser may be one of {@link metamer.fasta.Fasta} of {@link metamer.fastq.FastQ}
     *
     * @return Parser.
     */
    public Parser<T> parser() {
        return this.parser;
    }

    /**
     * Get sequence id.
     *
     * @return Id.
     */
    public String id() {
        return this.id;
    }
}
