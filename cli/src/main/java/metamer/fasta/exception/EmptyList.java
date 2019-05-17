package metamer.fasta.exception;

public class EmptyList extends Exception {
    public EmptyList() {
        super("Provided list of input strings is empty, expected correct list");
    }
}
