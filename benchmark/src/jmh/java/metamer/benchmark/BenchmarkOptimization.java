package metamer.benchmark;

import metamer.graph.Graph;
import metamer.graph.Node;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static metamer.graph.Graph.graph;

@State(Scope.Benchmark)
public class BenchmarkOptimization {
    private Graph gr;

    @Setup(Level.Trial)
    public void setupGraph() {
        gr = graph(
                3,
                Map.entry(new Node("AT"), new Node("TG")),
                Map.entry(new Node("TG"), new Node("GG")),
                Map.entry(new Node("GG"), new Node("GC")),
                Map.entry(new Node("GC"), new Node("CG")),
                Map.entry(new Node("CG"), new Node("GT")),
                Map.entry(new Node("GT"), new Node("TG")),
                Map.entry(new Node("TG"), new Node("GC")),
                Map.entry(new Node("GC"), new Node("CA"))
        );
    }

    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Benchmark
    public void testOptimization() {
//        gr = graph(
//                3,
//                Map.entry(new Node("AT"), new Node("TG")),
//                Map.entry(new Node("TG"), new Node("GG")),
//                Map.entry(new Node("GG"), new Node("GC")),
//                Map.entry(new Node("GC"), new Node("CG")),
//                Map.entry(new Node("CG"), new Node("GT")),
//                Map.entry(new Node("GT"), new Node("TG")),
//                Map.entry(new Node("TG"), new Node("GC")),
//                Map.entry(new Node("GC"), new Node("CA"))
//        );
        gr.optimizeGraph();
    }
}
