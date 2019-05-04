package metamer.functional.tests;

import metamer.cmdparser.CliHandler;
import metamer.cmdparser.CliHandlerMessages;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static metamer.functional.tests.Utils.temporaryFile;
import static metamer.functional.tests.Utils.temporaryPath;
import static metamer.functional.tests.Utils.temporaryDirectory;
import static metamer.utils.Strings.multiline;
import static java.nio.file.attribute.PosixFilePermissions.asFileAttribute;
import static java.nio.file.attribute.PosixFilePermissions.fromString;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.io.FileMatchers.anExistingFile;

public class FunctionalTest {
    private final static PrintStream standardOut = System.out;
    private final static InputStream standardIn = System.in;
    private final OutputStream testOut = new ByteArrayOutputStream();
    private final String newLine = System.lineSeparator();
    private final String usage = multiline(
            "usage: java metamer.jar [-f <arg>] [-h] [-i <arg>] [-k <arg>] [-o <arg>]",
            "Options",
            "   -f,--format <arg>     Format of input data: fasta or fastq",
            "   -h,--help             Present help",
            "   -i,--input <arg>      Input file with reads to be analyzed",
            "   -k <arg>              Length of k mer in De Bruijn graph",
            "   -o,--output <arg>     Output file to write result to",
            "-- HELP --");
    private final String content = multiline(
            ">id0 test",
            "ABCDEA",
            ">id1 test",
            "DEAB"
    );

    @BeforeEach
    public void setUpStream() {
        System.setOut(new PrintStream(testOut));
        System.setIn(new ByteArrayInputStream(content.getBytes()));
    }

    @AfterEach
    public void restoreStream() {
        System.setOut(standardOut);
        System.setIn(standardIn);
    }

    @Test
    @DisplayName("help option should be shown when there is no command in cmd")
    public void emptyInputTest() {
        CliHandler.main();
        assertThat(testOut.toString(), is(usage));
    }

    @Test
    @DisplayName("message should be shown when there is no key -k")
    public void noLengthTest() {
        CliHandler.main("-f", "fasta");
        assertThat(testOut.toString(), is(CliHandlerMessages.NO_LENGTH + newLine));
    }

    @Test
    @DisplayName("message should be shown when there is no key -f")
    public void noFormatTest() {
        CliHandler.main("-k", "3");
        assertThat(testOut.toString(), is(CliHandlerMessages.NO_FORMAT + newLine));
    }

    @Test
    @DisplayName("help option should be shown when there is -h key")
    public void helpTest() {
        CliHandler.main("-h");
        assertThat(testOut.toString(), is(usage));
    }

    @Test
    @DisplayName("help option should be shown when there is --help key")
    public void longHelpTest() {
        CliHandler.main("--help");
        assertThat(testOut.toString(), is(usage));
    }

    @Test
    @DisplayName("message should be shown when input file is directory")
    public void directoryAsInputFileTest() throws IOException {
        final Path inputPath = temporaryDirectory("inp");
        final Path outputPath = temporaryFile("out", ".fasta");

        final String expected = inputPath.toString() + CliHandlerMessages.PATH_IS_DIRECTORY + newLine;

        CliHandler.main("-k", "3", "-format", "fasta", "-i", inputPath.toString(), "-o", outputPath.toString());
        assertThat(testOut.toString(), is(expected));
    }

    @Test
    @DisplayName("message should be shown when output file is directory")
    public void directoryAsOutputFileTest() throws IOException {
        final Path inputPath = temporaryFile("inp", ".fasta");
        final Path outputPath = temporaryDirectory("out");

        final String expected = outputPath.toString() + CliHandlerMessages.PATH_IS_DIRECTORY + newLine;

        CliHandler.main("-k", "3", "-format", "fasta", "-i", inputPath.toString(), "-o", outputPath.toString());
        assertThat(testOut.toString(), is(expected));
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    @DisplayName("message should be shown when input file is not readable")
    public void notReadableInputFileTest() throws IOException {
        final Path inputPath = Files.createTempFile("inaccessible", ".fasta",
                asFileAttribute(fromString("---------")));
        final Path outputPath = temporaryFile("out", "fasta");

        final String expected = inputPath.toString() + CliHandlerMessages.FILE_IS_NOT_READABLE + newLine;

        CliHandler.main("-k", "3", "-format", "fasta", "-i", inputPath.toString(), "-o", outputPath.toString());
        assertThat(testOut.toString(), is(expected));
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    @DisplayName("message should be shown when output file is not writable")
    public void notWritableOutputFileTest() throws IOException {
        final Path inputPath = temporaryFile("inp", ".fasta");
        final Path inaccessible = temporaryDirectory("inaccessible", asFileAttribute(fromString("---------")));
        final Path outputPath = inaccessible.resolve("out.fasta");

        final String expected = outputPath.toString() + CliHandlerMessages.FILE_IS_NOT_WRITABLE + newLine;

        CliHandler.main("-k", "3", "-format", "fasta", "-i", inputPath.toString(), "-o", outputPath.toString());
        assertThat(testOut.toString(), is(expected));
    }

    @Test
    @DisplayName("message should be shown when input file doesn't exist")
    public void inputFileDoesNotExistTest() throws IOException {
        final Path nonexistentPath = Paths.get("SomeNonexistentInputFile.fasta");
        final Path outputPath = temporaryFile("out", ".fasta");

        final String expected = nonexistentPath.toString() + CliHandlerMessages.FILE_DOES_NOT_EXIST + newLine;

        CliHandler.main("-k", "3", "-format", "fasta", "-i", nonexistentPath.toString(), "-o", outputPath.toString());
        assertThat(testOut.toString(), is(expected));
    }

    @Test
    @DisplayName("message should be shown when output file already exists")
    public void outputFileAlreadyExistTest() throws IOException {
        final Path inputPath = temporaryFile("inp", ".fasta");
        final Path outputPath = temporaryFile("out", ".fasta");

        final String expected = outputPath.toString() + CliHandlerMessages.FILE_ALREADY_EXIST + newLine;

        CliHandler.main("-k", "3", "-format", "fasta", "-i", inputPath.toString(), "-o", outputPath.toString());
        assertThat(testOut.toString(), is(expected));
    }

    @Test
    @DisplayName("project should work in a correct way when ran")
    public void correctInputTest() throws IOException {
        final Path inputPath = temporaryFile("inp", ".fasta");
        Files.write(inputPath, content.getBytes());

        final Path outputPath = temporaryPath("out", ".fasta");

        final String expected1 = ">cycle from file: " + inputPath.toString();
        final String expected2 = "DEABCD";

        CliHandler.main("-k", "3", "-format", "fasta", "-i", inputPath.toString(), "-o", outputPath.toString());
        assertThat(Files.lines(outputPath).collect(toList()), contains(expected1, expected2));
    }

    @Disabled
    @Test
    @DisplayName("stdout should contain expected string when there is no output file")
    public void writeInStdoutTest() throws IOException {
        final Path inputPath = temporaryFile("inp", ".fasta");
        Files.write(inputPath, content.getBytes());

        final String expected1 = ">cycle from file: " + inputPath.toString();
        final String expected2 = "DEABCD";

        CliHandler.main("-k", "3", "-format", "fasta", "-i", inputPath.toString());
        assertThat(testOut.toString(), containsString(expected1));
        assertThat(testOut.toString(), containsString(expected2));
    }

    @Disabled
    @Test
    @DisplayName("stdin should be a source of information when there is no input file")
    public void readingFromStdinTest() throws IOException {
        final Path outputPath = temporaryPath("out", ".fasta");

        final String expected1 = ">cycle from file: " + null;
        final String expected2 = "DEABCD";

        CliHandler.main("-k", "3", "-format", "fasta", "-o", outputPath.toString());
        assertThat(outputPath.toFile(), anExistingFile());
        assertThat(Files.lines(outputPath).collect(toList()), contains(expected1, expected2));
    }

    @Disabled
    @Test
    @DisplayName("stdin should be source & stdout should contain result when there is no input & output files")
    public void readingFromStdinWriteInStdoutTest() {
        final String expected1 = ">cycle from file: " + null;
        final String expected2 = "DEABCD";

        CliHandler.main("-k", "3", "-format", "fasta");
        assertThat(testOut.toString(), containsString(expected1));
        assertThat(testOut.toString(), containsString(expected2));
    }
}
