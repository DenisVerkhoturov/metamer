package metamer.cmdparser.exception;

import java.nio.file.Path;
import java.util.Objects;

public class FileIsNotReadable extends Exception {
    private final Path path;

    public FileIsNotReadable(final Path path) {
        super("Provided file is not readable, a readable file is expected: " + path);
        this.path = path;
    }

    public Path path() {
        return this.path;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileIsNotReadable that = (FileIsNotReadable) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
