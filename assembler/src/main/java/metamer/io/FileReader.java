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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.stream.Stream;

/**
 * Class for reading from file.
 *
 * @param <T> Type of Record: {@link metamer.fasta.Record} or {@link metamer.fastq.Record}.
 */
@lombok.Value
public class FileReader<T> implements Reader {
    private final Path path;
    private final Parser<T> parser;

    /**
     * Function for reading from file.
     *
     * @return Stream of records for further work.
     */
    public Stream<T> read() {
        try (final Stream<String> lines = Files.lines(path)) {
            final Either<Exception, Seq<T>> value = this.parser().read(lines);
            return value.map(Value::toJavaStream).getOrElse(Stream.empty());
        } catch (final IOException e) {
            throw new RuntimeException("Can't parse :(");
        }
    }


    /**
     * Get path to file.
     *
     * @return Path to file.
     */
    public Path path() {
        return path;
    }

    /**
     * Get current parser.
     * <p>
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
     * @return Id in future.
     */
    public String id() {
        return this.path.toString();
    }

}
