package metamer.cmdparser.exception;

import java.util.Objects;

public class InvalidFormat extends Exception {
    private final String format;

    public InvalidFormat(final String format) {
        super("Provided format of data is invalid, a correct format is expected: " + format);
        this.format = format;
    }

    public String format() {
        return this.format;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvalidFormat that = (InvalidFormat) o;
        return Objects.equals(format, that.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(format);
    }
}
