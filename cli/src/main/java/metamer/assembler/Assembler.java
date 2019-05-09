package metamer.assembler;

import metamer.fasta.Record;
import metamer.graph.Graph;
import metamer.graph.GraphCycle;
import metamer.io.Reader;
import metamer.io.Writer;

import java.util.HashMap;
import java.util.stream.Stream;

public class Assembler {
    private final Reader reader;
    private final Writer writer;
    private final int k;

    public Assembler(final Reader reader, final Writer writer, final int k) {
        this.reader = reader;
        this.writer = writer;
        this.k = k;
    }

    public void assemble() {
        final Stream<Record> records = reader.read();

        Graph graph = new Graph(new HashMap<>(), new HashMap<>(), k);
        graph.createFromStream(records.map(record -> record.sequence));

        final GraphCycle graphCycle = new GraphCycle(graph.optimizeGraph(), k);
        String cycle = graphCycle.findCycle();

        writer.write(Stream.of(new Record("cycle", "from file: " + reader.id(), cycle)));
    }
}

