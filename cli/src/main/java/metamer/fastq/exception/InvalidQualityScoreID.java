package metamer.fastq.exception;

import java.util.Objects;

public class InvalidQualityScoreID extends Exception {
    private final String qualityScoreID;

    public InvalidQualityScoreID(final String qualityScoreID) {
        super("Provided fastQ quality score ID is invalid, a correct quality score ID is expected: " + qualityScoreID);
        this.qualityScoreID = qualityScoreID;
    }

    public String sequenceLine() {
        return this.qualityScoreID;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvalidQualityScoreID that = (InvalidQualityScoreID) o;
        return Objects.equals(qualityScoreID, that.qualityScoreID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualityScoreID);
    }
}


