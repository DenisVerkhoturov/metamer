package metamer.io;

import java.io.BufferedReader;
import java.io.IOException;
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
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            return this.parser().read(stdin.lines());
        } catch (final IOException e) {
            throw new RuntimeException("Can't parse :(");
        }
    }

    public Parser<T> parser() {
        return this.parser;
    }

    public String id() {
        return this.id;
    }
}
