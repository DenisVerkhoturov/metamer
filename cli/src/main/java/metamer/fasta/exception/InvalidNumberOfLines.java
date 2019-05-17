package metamer.fasta.exception;

import java.util.Objects;

public class InvalidNumberOfLines extends Exception {
    private final int numberOfLines;

    public InvalidNumberOfLines(final int numberOfLines) {
        super("Provided number of lines is invalid, expected two lines at least: " + numberOfLines);
        this.numberOfLines = numberOfLines;
    }

    public int numberOfLines() {
        return this.numberOfLines;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvalidNumberOfLines that = (InvalidNumberOfLines) o;
        return numberOfLines == that.numberOfLines;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfLines);
    }
}
