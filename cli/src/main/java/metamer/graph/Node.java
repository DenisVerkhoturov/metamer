package metamer.graph;

import java.util.Objects;

public class Node {
    public String kmer;
    public int nin;
    public int nout;

    Node(final String kmer) {
        this.kmer = kmer;
        this.nin = 0;
        this.nout = 0;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Node node = (Node)obj;
        return Objects.equals(this.kmer, node.kmer) && Objects.equals(this.nin, node.nin) &&
                Objects.equals(this.nout, node.nout);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.kmer);
    }

    @Override
    public String toString() {
        //return " Kmer = " + this.kmer + ", nout = " + this.nout + ", nin = " + this.nin + " ";
        return this.kmer;
    }

}
