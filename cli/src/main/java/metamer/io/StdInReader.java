package metamer.io;

import io.vavr.Value;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class StdInReader<T> implements Reader {
    private String id;
    private Parser<T> parser;

    public StdInReader(final Parser<T> parser) {
        this.id = System.in.toString();
        this.parser = parser;
    }

    public Stream<T> read() {
        try (final Stream<String> lines = new BufferedReader(new InputStreamReader(System.in)).lines()) {
            final Either<Exception, Seq<T>> value = this.parser().read(lines);
            return value.map(Value::toJavaStream).getOrElse(Stream.empty());
        }
    }

    public Parser<T> parser() {
        return this.parser;
    }

    public String id() {
        return this.id;
    }
}
