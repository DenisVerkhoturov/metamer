package metamer.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface Parser<T> {
    Stream<String> show(T record);
    Stream<String> show(Stream<T> records);
    T read(List<String> lines);
    Stream<T> read(Path inputPath) throws IOException;
}
