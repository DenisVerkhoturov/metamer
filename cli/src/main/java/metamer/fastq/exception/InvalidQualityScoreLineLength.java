package metamer.fastq.exception;

import java.util.Objects;

public class InvalidQualityScoreLineLength extends Exception {
    private final int qualityScoreLineLength;

    public InvalidQualityScoreLineLength(final int qualityScoreLineLength) {
        super("Provided fastQ quality score line length is invalid, " +
                "a correct quality score line length is expected" +
                qualityScoreLineLength);
        this.qualityScoreLineLength = qualityScoreLineLength;
    }

    public int qualityScoreLineLength() {
        return this.qualityScoreLineLength;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvalidQualityScoreLineLength that = (InvalidQualityScoreLineLength) o;
        return qualityScoreLineLength == that.qualityScoreLineLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualityScoreLineLength);
    }
}
