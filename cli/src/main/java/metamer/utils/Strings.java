package metamer.utils;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.Arrays.stream;

public class Strings {
    private final static String newLine = System.lineSeparator();

    public static String multiline(final String... lines) {
        return stream(lines).collect(joining(newLine, "", newLine));
    }

    public static Stream<String> windows(final String str, final int size) {
        return size > str.length()
                ? Stream.empty()
                : IntStream.rangeClosed(0, str.length() - size)
                .mapToObj(start -> str.substring(start, start + size));
    }
}
