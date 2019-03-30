package metamer.utils;

import java.nio.file.Path;

public class Paths {
    public static String extension(final Path path) {
        String s = path.getFileName().toString();
        final int index = s.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return s.substring(index + 1);
    }
}
