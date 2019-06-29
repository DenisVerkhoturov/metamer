package metamer.benchmark;

import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;

public class BenchmarkRunner {
    public static void main(final String[] args) throws IOException, RunnerException {
        org.openjdk.jmh.Main.main(args);
    }
}
