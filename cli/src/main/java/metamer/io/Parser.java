package metamer.io;

import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.List;
import java.util.stream.Stream;

public interface Parser<T> {
    Stream<String> show(T record);
    Stream<String> show(Stream<T> records);
    Either<String, T> read(List<String> lines);
    Either<String, Seq<T>> read(Stream<String> lines);
}
