package metamer.io;

import java.util.stream.Stream;

public interface Writer<T> {
    void write(Stream<T> lines);
    Parser<T> parser();
    String id();
}
