package metamer.utils;

import java.net.URISyntaxException;
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

    public static Path resource(final String filename) throws URISyntaxException {
        return resource(Paths.class, filename);
    }

    public static Path resource(final Class clazz, final String filename) throws URISyntaxException {
        return java.nio.file.Paths.get(clazz.getResource(filename).toURI());
    }
}
