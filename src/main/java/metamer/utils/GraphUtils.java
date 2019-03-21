package metamer.utils;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GraphUtils {
    public static Stream<String> slidingWindow(final String str, final int size) {
        return size > str.length()
                ? Stream.empty()
                : IntStream.rangeClosed(0, str.length() - size)
                .mapToObj(start -> str.substring(start, start + size));
    }
}

