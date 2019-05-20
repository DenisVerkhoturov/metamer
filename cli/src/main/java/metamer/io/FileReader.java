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

public class FileReader<T> implements Reader {
    private Path path;
    private Parser<T> parser;

    public FileReader(final Path path, final Parser<T> parser) {
        this.path = path;
        this.parser = parser;
    }

    public Stream<T> read() {
        try (final Stream<String> lines = Files.lines(path)) {
            final Either<Exception, Seq<T>> value = this.parser().read(lines);
            return value.map(Value::toJavaStream).getOrElse(Stream.empty());
        } catch (final IOException e) {
            throw new RuntimeException("Can't parse :(");
        }
    }

    public Path path() {
        return path;
    }

    public Parser<T> parser() {
        return this.parser;
    }

    public String id() {
        return this.path.toString();
    }
}
