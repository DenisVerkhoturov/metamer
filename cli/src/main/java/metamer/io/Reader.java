package metamer.io;

import java.util.stream.Stream;

public interface Reader<T> {
    Stream<T> read();
    Parser<T> parser();
}
