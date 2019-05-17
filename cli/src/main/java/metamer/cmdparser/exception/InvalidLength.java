package metamer.cmdparser.exception;

import java.util.Objects;

public class InvalidLength extends Exception {
    private final String length;

    public InvalidLength(final String length) {
        super("Provided length of kmers is invalid, a correct length is expected: " + length);
        this.length = length;
    }

    public String length() {
        return this.length;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvalidLength that = (InvalidLength) o;
        return Objects.equals(length, that.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(length);
    }
}
