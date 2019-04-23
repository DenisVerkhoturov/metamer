package metamer.fastq;

import java.util.Arrays;
import java.util.Objects;

public class Record {
    public final String id;
    public final String description;
    public final String sequence;
    public final byte[] quality;

    public Record(final String id, final String description, final String sequence, final byte[] quality) {
        this.id = id;
        this.description = description;
        this.sequence = sequence;
        this.quality = quality;
    }

    @Override
    public  boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Record rec = (Record)obj;
        return (this.id.equals(rec.id) & this.description.equals(rec.description) &
                this.sequence.equals(rec.sequence) & Arrays.equals(this.quality, rec.quality));
    }

    @Override
    public String toString() {
        return "ID: " + this.id + "; description: " + this.description + "; sequence: " +
                this.sequence + " quality: " + this.quality;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id + description + sequence + quality);
    }
}

