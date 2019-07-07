package metamer.benchmark;

import metamer.cmdparser.CliHandler;
import metamer.io.FileReader;
import metamer.io.HasSequence;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.TearDown;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static metamer.utils.Strings.multiline;

@State(Scope.Benchmark)
@Fork(value = 1, warmups = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class BenchmarkParsing {
    private Path inputPath;
    private Path outputPath;
    private int k;
    private String formatS;
    private CliHandler.Format formatF;

    private final String content = multiline(
            ">id0 test",
            "ATGGCGTGCD"
    );

    @Setup(Level.Trial)
    public void setup() throws IOException {
        inputPath = Files.createTempFile("out", null);
        inputPath.toFile().deleteOnExit();
        Files.write(inputPath, content.getBytes());

        outputPath = Files.createTempFile("out", null);
        outputPath.toFile().deleteOnExit();

        k = 3;

        formatS = "fasta";

        formatF = CliHandler.validateFormat(formatS).get();

        System.setIn(new ByteArrayInputStream(content.getBytes()));
    }

    @TearDown
    public void resetIn() {
        System.setIn(System.in);
    }

    @Benchmark
    public void benchmarkParsingFromFileToFile() {
        CliHandler.validateFromFileToFile(inputPath.toString(), outputPath.toString(), String.valueOf(k), formatS);
    }

    @Benchmark
    public void benchmarkParsingFromFileToStd() {
        CliHandler.validateFromFileToStd(inputPath.toString(), String.valueOf(k), formatS);
    }

    @Benchmark
    public void benchmarkParseInputPath() {
        CliHandler.validateInputPath(inputPath.toString());
    }

    @Benchmark
    public void benchmarkParseOutputPath() {
        CliHandler.validateOutputPath(outputPath.toString());
    }

    @Benchmark
    public void benchmarkParseK() {
        CliHandler.validateK(String.valueOf(k));
    }

    @Benchmark
    public void benchmarkParseFormat() {
        CliHandler.validateFormat(formatS);
    }

    @Benchmark
    public void benchmarkReader() {
        new FileReader<>(inputPath, formatF.parser)
                .read()
                .map(HasSequence::sequence);
    }
}
