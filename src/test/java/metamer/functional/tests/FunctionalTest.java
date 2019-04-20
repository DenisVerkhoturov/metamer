package metamer.functional.tests;

import metamer.cmdparser.CliHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

import static java.nio.file.attribute.PosixFilePermissions.asFileAttribute;
import static java.nio.file.attribute.PosixFilePermissions.fromString;
import static metamer.utils.Strings.multiline;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.io.FileMatchers.anExistingFile;

public class FunctionalTest {
    private final static PrintStream standardOut = System.out;
    private final OutputStream testOut = new ByteArrayOutputStream();
    private final String newLine = System.lineSeparator();
    private final String usage = multiline(
            "usage: java metamer.jar [-c <arg>] [-f <arg>] [-h]",
            "Options",
            "   -c,--command <arg>      Which command to do",
            "   -f,--filepath <arg>     Path to file",
            "   -h,--help               Present help",
            "-- HELP --");
    private final String content = multiline(
            ">id0 test",
            "ABCDEA",
            ">id1 test",
            "DEAB"
    );

    private static Path temporaryFile(final String name, final String suffix, final FileAttribute... attributes)
            throws IOException {
        final Path outputPath = Files.createTempFile(name, suffix, attributes);
        outputPath.toFile().deleteOnExit();
        return outputPath;
    }

    private static Path temporaryDirectory(final String name, final FileAttribute... attributes) throws IOException {
        final Path outputPath = Files.createTempDirectory(name, attributes);
        outputPath.toFile().deleteOnExit();
        return outputPath;
    }

    @BeforeEach
    public void setUpStream() {
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    public void restoreStream() {
        System.setOut(standardOut);
    }

    @Test
    public void emptyInputTest() {
        CliHandler.main();
        assertThat(testOut.toString(), is(usage));
    }

    @Test
    public void helpTest() {
        CliHandler.main("-h");
        assertThat(testOut.toString(), is(usage));
    }

    @Test
    public void longHelpTest() {
        CliHandler.main("--help");
        assertThat(testOut.toString(), is(usage));
    }

    @Test
    public void directoryAsInputFileTest() throws IOException {
        final Path inputPath = temporaryDirectory("inp");
        final Path outputPath = temporaryFile("out", ".fasta");

        final String expected = inputPath.toString() + CliHandler.Messages.PATH_IS_DIRECTORY + newLine;

        CliHandler.main(inputPath.toString(), outputPath.toString());
        assertThat(testOut.toString(), is(expected));
    }

    @Test
    public void directoryAsOutputFileTest() throws IOException {
        final Path inputPath = temporaryFile("inp", ".fasta");
        final Path outputPath = temporaryDirectory("out");

        final String expected = outputPath.toString() + CliHandler.Messages.PATH_IS_DIRECTORY + newLine;

        CliHandler.main(inputPath.toString(), outputPath.toString());
        assertThat(testOut.toString(), is(expected));
    }

    @Test
    public void notReadableInputFileTest() throws IOException {
        final Path inputPath = Files.createTempFile("inaccessible", ".fasta",
                asFileAttribute(fromString("---------")));
        final Path outputPath = temporaryFile("out", "fasta");

        final String expected = inputPath.toString() + CliHandler.Messages.FILE_IS_NOT_READABLE + newLine;

        CliHandler.main(inputPath.toString(), outputPath.toString());
        assertThat(testOut.toString(), is(expected));
    }

    @Test
    public void notWritableOutputFileTest() throws IOException {
        final Path inputPath = temporaryFile("inp", ".fasta");
        final Path outputPath = temporaryFile("inaccessible", ".fasta",
                asFileAttribute(fromString("---------")));
        outputPath.toFile().delete();

        final String expected = outputPath.toString() + CliHandler.Messages.FILE_IS_NOT_WRITABLE + newLine;

        CliHandler.main(inputPath.toString(), outputPath.toString());
        assertThat(testOut.toString(), is(expected));
    }

    @Test
    public void inputFileDoesNotExistTest() throws IOException {
        final Path nonexistentPath = Paths.get("SomeNonexistentInputFile.fasta");
        final Path outputPath = temporaryFile("out", ".fasta");

        final String expected = nonexistentPath.toString() + CliHandler.Messages.FILE_DOES_NOT_EXIST + newLine;

        CliHandler.main(nonexistentPath.toString(), outputPath.toString());
        assertThat(testOut.toString(), is(expected));
    }

    @Test
    public void outputFileAlreadyExistTest() throws IOException {
        final Path inputPath = temporaryFile("inp", ".fasta");
        final Path outputPath = temporaryFile("out", ".fasta");

        final String expected = outputPath.toString() + CliHandler.Messages.FILE_ALREADY_EXIST + newLine;

        CliHandler.main(inputPath.toString(), outputPath.toString());
        assertThat(testOut.toString(), is(expected));
    }

    @Disabled("To fix")
    @Test
    public void correctInputTest() throws IOException {
        final Path inputPath = temporaryFile("inp", ".fasta");
        Files.write(inputPath, content.getBytes());
        final Path outputPath = temporaryFile("out", ".fasta");
        outputPath.toFile().delete();

        final String expected1 = ">cycle from file: " + inputPath.toString();
        final String expected2 = "ABCDE";

        CliHandler.main(inputPath.toString(), outputPath.toString());
        assertThat(outputPath.toFile(), anExistingFile());
        assertThat(Files.lines(outputPath).collect(toList()), contains(expected1, expected2));
    }

    @Disabled("Writing to stdout is not implemented yet")
    @Test
    public void writeInStdoutTest() throws IOException {
        final Path inputPath = temporaryFile("inp", ".fasta");
        Files.write(inputPath, content.getBytes());

        final String expected1 = ">cycle from file: " + inputPath.toString();
        final String expected2 = "ABCDE";

        CliHandler.main(inputPath.toString(), "-stdout");
        assertThat(testOut.toString(), containsString(expected1));
        assertThat(testOut.toString(), containsString(expected2));
    }

    @Disabled("Reading from stdin is not implemented yet")
    @Test
    public void readingFromStdinTest() throws IOException {
        final Path outputPath = temporaryFile("out", ".fasta");
        outputPath.toFile().delete();

        final String expected = ">cycle from file: " + System.in.toString();

        CliHandler.main("-stdin", outputPath.toString());
        assertThat(outputPath.toFile(), anExistingFile());
        assertThat(Files.lines(outputPath).collect(toList()), contains(expected));
    }

    @Disabled("Reading from stdin and writing to stdout are not implemented yet")
    @Test
    public void readingFromStdinWriteInStdoutTest() {
        final String expected = ">cycle from file: " + System.in.toString();

        CliHandler.main("-stdin", "-stdout");
        assertThat(testOut.toString(), containsString(expected));
    }
}
