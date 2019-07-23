package metamer.io;

import java.nio.file.Path;
import java.util.stream.Stream;

public abstract class Decorator<T> implements Reader {
    protected Reader reader;
    protected final Path path;
    protected final Parser<T> parser;

    public Decorator (final Reader r, final Path p, final Parser<T> parser) {
        reader = r;
        path = p;
        this.parser = parser;
    }

    abstract public Stream<T> read();

    @Override
    public Parser<T> parser() {
        return this.parser;
    }

    @Override
    public String id() {
        return this.path.toString();
    }
}
