package metamer.cmdparser;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class CliHandlerTest {
    private final String newLine = System.lineSeparator();

    @Test
    @DisplayName("parse exception should be thrown when there is no argument after key -k")
    public void nullLengthTest() {
        String[] args = new String[] {"-k", "-f", "fasta"};

        Throwable thrown = assertThrows(ParseException.class, () ->
                CliHandler.parse(args));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("parse exception should be thrown when there is no argument after key -f")
    public void nullFormatTest() {
        String[] args = new String[] {"-k", "abc", "-f"};

        Throwable thrown = assertThrows(ParseException.class, () ->
                CliHandler.parse(args));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("parse exception should be thrown when there is no argument after key -i")
    public void nullInputPathTest() {
        String[] args = new String[] {"-k", "abc", "-f", "fasta", "-i"};

        Throwable thrown = assertThrows(ParseException.class, () ->
                CliHandler.parse(args));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("parse exception should be thrown when there is no argument after key -o")
    public void nullOutputPathTest() {
        String[] args = new String[] {"-k", "abc", "-f", "fasta", "-o"};

        Throwable thrown = assertThrows(ParseException.class, () ->
                CliHandler.parse(args));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("number format exception should be thrown when key's -k argument is not integer")
    public void invalidLengthTest() {
        String[] args = new String[] {"-k", "abc", "-f", "fasta"};

        Throwable thrown = assertThrows(NumberFormatException.class, () ->
                CliHandler.parse(args));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("message should be shown when key's -f argument is not fasta or fastq")
    public void invalidFormatTest() {
        PrintStream standardOut = System.out;
        OutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        CliHandler.main("-k", "3", "-f", "fast");
        assertThat(testOut.toString(), is(CliHandlerMessages.INVALID_FORMAT + newLine));

        System.setOut(standardOut);
    }
}
