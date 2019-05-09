package metamer.io;

import java.util.stream.Stream;

public class StdOutWriter<T> implements Writer<T> {
    private String id;
    private Parser<T> parser;

    public StdOutWriter(final Parser<T> parser) {
        this.id = System.out.toString();
        this.parser = parser;
    }

    public void write(final Stream<T> records) {
        final Stream<String> lines = this.parser().show(records);
        lines.forEach(System.out::println);
    }

    public Parser<T> parser() {
        return this.parser;
    }

    public String id() {
        return this.id;
    }
}
