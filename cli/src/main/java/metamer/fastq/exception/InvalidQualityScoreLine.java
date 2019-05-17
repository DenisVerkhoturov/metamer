package metamer.fastq.exception;

import java.util.Objects;

public class InvalidQualityScoreLine extends Exception {
    private final String qualityScoreLine;

    public InvalidQualityScoreLine(final String qualityScoreLine) {
        super("Provided fastQ quality score line contains invalid symbols, " +
                "a correct quality score line is expected" + qualityScoreLine);
        this.qualityScoreLine = qualityScoreLine;
    }

    public String qualityScoreLine() {
        return this.qualityScoreLine;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvalidQualityScoreLine that = (InvalidQualityScoreLine) o;
        return Objects.equals(qualityScoreLine, that.qualityScoreLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualityScoreLine);
    }
}
