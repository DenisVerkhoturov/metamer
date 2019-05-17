package metamer.cmdparser.exception;

import java.util.Objects;

public class NoFormat extends Exception {
    private final String format;

    public NoFormat() {
        super("No format of data was provided, a correct format must be provided");
        this.format = null;
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
        NoFormat noFormat = (NoFormat) o;
        return Objects.equals(format, noFormat.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(format);
    }
}
