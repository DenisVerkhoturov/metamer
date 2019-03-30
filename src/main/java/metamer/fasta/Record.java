package metamer.fasta;

import java.util.Objects;

public class Record {
    public final String uniqueIdentifier;
    public final String additionalInformation;
    public final String sequence;

    public Record(String uniqueIdentifier, String additionalInformation, String sequence) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.additionalInformation = additionalInformation;
        this.sequence = sequence;
    }

    public Record(String uniqueIdentifier, String sequence) {
        this(uniqueIdentifier, "", sequence);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(uniqueIdentifier, record.uniqueIdentifier) &&
                Objects.equals(additionalInformation, record.additionalInformation) &&
                Objects.equals(sequence, record.sequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueIdentifier, additionalInformation, sequence);
    }

    @Override
    public String toString() {
        return "Record{" +
                "uniqueIdentifier='" + uniqueIdentifier + '\'' +
                ", additionalInformation='" + additionalInformation + '\'' +
                ", sequence='" + sequence + '\'' +
                '}';
    }
}
