package metamer.io;

import io.vavr.Value;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.io.BufferedReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class DecoratorGZIPStd<T> extends Decorator {
    public DecoratorGZIPStd(final Reader r, final Path p, final Parser<T> pars) {
        super(r, p, pars);
    }

    @Override
    public Stream<T> read() {
        try (final Stream<String> lines = new BufferedReader(new InputStreamReader
                (new GZIPInputStream(new ByteArrayInputStream(System.in.readAllBytes())))).lines()) {
            final Either<Exception, Seq<T>> value = this.parser().read(lines);
            return value.map(Value::toJavaStream).getOrElse(Stream.empty());
        } catch (final IOException e) {
            throw new RuntimeException("Can't parse :(");
        }
    }
}
