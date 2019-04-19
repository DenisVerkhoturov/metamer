package metamer.assembler;

import metamer.fasta.Fasta;
import metamer.fasta.Record;
import metamer.graph.Graph;
import metamer.graph.GraphCycle;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static metamer.fasta.Fasta.FASTA_EXTENSION;

public class Assembler {
    private final Path inputFile;
    private final Path outputFile;

    public Assembler(Path inputFile, Path outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void assemble() {
        try {
            if (metamer.utils.Paths.extension(inputFile).equals(FASTA_EXTENSION)) {
                final Stream<Record> records = new Fasta(inputFile).records();
                Graph graph = new Graph(new HashMap<>(), new HashMap<>(), 3);
                graph.createFromStream(records.map(record -> record.sequence));
                final GraphCycle graphCycle = new GraphCycle(graph.optimizeGraph());
                String cycle = graphCycle.findCycle();

                new Fasta(outputFile).write(List.of(new Record("cycle", "from file: " + inputFile, cycle)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
