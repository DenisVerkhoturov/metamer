package metamer.fasta.exception;

import java.util.Objects;

public class InvalidIdentifier extends Exception {
    private final char identifier;

    public InvalidIdentifier(final char identifier) {
        super("Provided identifier is invalid, a correct identifier is expected: " + identifier);
        this.identifier = identifier;
    }

    public char identifier() {
        return this.identifier;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvalidIdentifier that = (InvalidIdentifier) o;
        return identifier == that.identifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
