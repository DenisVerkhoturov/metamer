package metamer.benchmark;

import metamer.graph.Graph;
import metamer.graph.GraphCycle;
import metamer.graph.Node;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static metamer.graph.Graph.graph;

@State(Scope.Benchmark)
@Fork(value = 1, warmups = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class BenchmarkAssembler {
    private Graph graph;
    private Graph optGraph;
    private GraphCycle graphCycle;
    private GraphCycle graphCycle2;

    @Setup
    public void setup() {
        graph = createData();
        optGraph = graph.optimizeGraph();
        graphCycle = createGraphCycle();
        graphCycle2 = createOptGraphCycle();
    }

    public Graph createData() {
        return graph(
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

    public GraphCycle createGraphCycle() {
        return new GraphCycle(graph, 3);
    }

    public GraphCycle createOptGraphCycle() {
        return new GraphCycle(optGraph, 3);
    }

    @Benchmark
    public void testPathAssembly() {
        graphCycle.findCycle();
    }

    @Benchmark
    public void testPathAssemblyInOptimizedGraph() {
        graphCycle2.findCycle();
    }

}
