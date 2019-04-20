package metamer.utils;

import static java.util.stream.Collectors.joining;
import static java.util.Arrays.stream;

public class Strings {
    private final static String newLine = System.lineSeparator();

    public static String multiline(final String... lines) {
        return stream(lines).collect(joining(newLine, "", newLine));
    }
}
