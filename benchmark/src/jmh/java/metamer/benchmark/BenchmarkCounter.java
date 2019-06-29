package metamer.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Param;


@State(Scope.Benchmark)
public class BenchmarkCounter {
    @Param({"1000000", "100"})
    private int N;

    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @Benchmark
    public void whileIteration() {
        int i = 0;
        while (i < N) {
            i++;
        }
    }
}
