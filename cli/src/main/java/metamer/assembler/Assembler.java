package metamer.assembler;

import metamer.fasta.Record;
import metamer.graph.Graph;
import metamer.graph.GraphCycle;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Assembler {
    private final Stream<String> reads;
    private final Consumer<Stream<Record>> writer;
    private final int k;

    public Assembler(final Stream<String> reads, final Consumer<Stream<Record>> writer, final int k) {
        this.reads = reads;
        this.writer = writer;
        this.k = k;
    }

    public void assemble() {
        Graph graph = new Graph(new HashMap<>(), new HashMap<>(), k);
        graph.createFromStream(reads);
        final GraphCycle graphCycle = new GraphCycle(graph.optimizeGraph(), k);
        writer.accept(Stream.of(new Record("FIX ME", "", graphCycle.findCycle())));
    }

    @Override
    public boolean equals(final Object another) {
        if (this == another) return true;
        if (another == null || getClass() != another.getClass()) return false;

        final Assembler that = (Assembler) another;

        return this.k == that.k && this.reads == that.reads && this.writer == that.writer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(k, reads, writer);
    }
}

