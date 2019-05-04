package metamer.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.security.SecureRandom;

public class CliHandlerUtils {

    public static Path temporaryDirectory(final String name, final FileAttribute... attributes) throws IOException {
        final Path outputPath = Files.createTempDirectory(name, attributes);
        outputPath.toFile().deleteOnExit();
        return outputPath;
    }

    public static Path temporaryPath(final String prefix, final String suffix) {
        return temporaryPath(Path.of(System.getProperty("java.io.tmpdir")), prefix, suffix);
    }

    public static Path temporaryPath(final Path parent, final String prefix, final String suffix) {
        final String random = Long.toUnsignedString(new SecureRandom().nextLong());
        final String name = prefix + random + suffix;
        final Path outputPath = parent.resolve(name);
        outputPath.toFile().deleteOnExit();
        return outputPath;
    }

    public static Path temporaryFile(final String name, final String suffix, final FileAttribute... attributes)
            throws IOException {
        final Path outputPath = Files.createTempFile(name, suffix, attributes);
        outputPath.toFile().deleteOnExit();
        return outputPath;
    }
}
