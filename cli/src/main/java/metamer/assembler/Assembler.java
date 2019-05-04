package metamer.assembler;

import metamer.fasta.Fasta;
import metamer.fasta.Record;
import metamer.graph.Graph;
import metamer.graph.GraphCycle;
import metamer.io.FileReader;
import metamer.io.FileWriter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Stream;

public class Assembler {
    private final Path inputFile;
    private final Path outputFile;
    private final int k;

    public Assembler(final Path inputFile, final Path outputFile, final int k) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.k = k;
    }

    public void assemble() {

        Stream<Record> records = Stream.of();
        if (inputFile != null) {
            FileReader<Record> t = new FileReader<>(inputFile, Fasta.parser());
            records = t.read();
        } else {
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            records = Fasta.parser().read(stdin.lines());
        }

        Graph graph = new Graph(new HashMap<>(), new HashMap<>(), k);
        graph.createFromStream(records.map(record -> record.sequence));

        final GraphCycle graphCycle = new GraphCycle(graph.optimizeGraph(), k);
        String cycle = graphCycle.findCycle();

        final Stream<Record> outRecords = Stream.of(new Record("cycle", "from file: " + inputFile, cycle));

        if (outputFile != null) {
            final FileWriter<Record> r = new FileWriter<>(outputFile, Fasta.parser());
            r.write(outRecords);
        } else {
            final Stream<String> lines = Fasta.parser().show(outRecords);
            lines.forEach(System.out::println);
        }
    }
}

