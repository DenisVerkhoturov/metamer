package metamer.cmdparser.exception;

import java.util.Objects;

public class NoLength extends Exception {
    private final String length;

    public NoLength() {
        super("No length of kmers was provided, a correct length must be provided");
        this.length = null;
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
        NoLength noLength = (NoLength) o;
        return Objects.equals(length, noLength.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(length);
    }
}
