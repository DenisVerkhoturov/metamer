package metamer.fastq.exception;

import java.util.Objects;

public class InvalidSequenceLine extends Exception {
    private final String sequenceLine;

    public InvalidSequenceLine(final String sequenceLine) {
        super("Provided fastQ sequence line is invalid, a correct sequence line is expected: " + sequenceLine);
        this.sequenceLine = sequenceLine;
    }

    public String sequenceLine() {
        return this.sequenceLine;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvalidSequenceLine that = (InvalidSequenceLine) o;
        return Objects.equals(sequenceLine, that.sequenceLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequenceLine);
    }
}
