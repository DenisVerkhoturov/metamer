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
