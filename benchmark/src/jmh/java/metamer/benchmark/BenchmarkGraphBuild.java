package metamer.benchmark;

import metamer.graph.Graph;
import metamer.graph.Node;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static metamer.graph.Graph.graph;

@State(Scope.Benchmark)
@Fork(value = 1, warmups = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkGraphBuild {

    @Benchmark
    public void fromEdges(final Blackhole blackhole) {
        Graph gr = graph(
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
        blackhole.consume(gr);
    }

    @Benchmark
    public void fromStream(final Blackhole blackhole) {
        Graph gr = graph(3, Stream.of(
                "I know an old lady who swallowed a spider",
                "That wriggled and jiggled and tickled inside her",
                "She swallowed the spider to catch the fly",
                "But I don't know why she swallowed the fly",
                "Perhaps she'll die"));
        blackhole.consume(gr);
    }
}
