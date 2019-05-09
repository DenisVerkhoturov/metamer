package metamer.cmdparser;

import io.vavr.control.Validation;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.IOException;
import java.nio.file.Path;

import static io.vavr.control.Validation.invalid;
import static java.nio.file.attribute.PosixFilePermissions.asFileAttribute;
import static java.nio.file.attribute.PosixFilePermissions.fromString;
import static metamer.utils.CliHandlerUtils.temporaryDirectory;
import static metamer.utils.CliHandlerUtils.temporaryFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CliHandlerTest {
    @Test
    @DisplayName("parse exception should be thrown when there is no argument after key -k")
    public void nullLengthTest() {
        String[] args = new String[] {"-k", "-f", "fasta"};

        Throwable thrown = assertThrows(ParseException.class, () -> CliHandler.parse(args));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("parse exception should be thrown when there is no argument after key -f")
    public void nullFormatTest() {
        String[] args = new String[] {"-k", "abc", "-f"};

        Throwable thrown = assertThrows(ParseException.class, () -> CliHandler.parse(args));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("parse exception should be thrown when there is no argument after key -i")
    public void nullInputPathTest() {
        String[] args = new String[] {"-k", "abc", "-f", "fasta", "-i"};

        Throwable thrown = assertThrows(ParseException.class, () -> CliHandler.parse(args));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("parse exception should be thrown when there is no argument after key -o")
    public void nullOutputPathTest() {
        String[] args = new String[] {"-k", "abc", "-f", "fasta", "-o"};

        Throwable thrown = assertThrows(ParseException.class, () -> CliHandler.parse(args));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("number format exception should be thrown when key's -k argument is not integer")
    public void invalidLengthTest() {
        final Validation<String, Integer> verdict = CliHandler.validateK("abc");
        assertThat(verdict, is(invalid(CliHandlerMessages.INVALID_LENGTH)));
    }

    @Test
    @DisplayName("message should be shown when key's -f argument is not fasta or fastq")
    public void invalidFormatTest() {
        final Validation<String, CliHandler.Format> verdict = CliHandler.validateFormat("fast");
        assertThat(verdict, is(invalid(CliHandlerMessages.INVALID_FORMAT)));
    }

    @Test
    @DisplayName("input path should be invalid if it is a directory")
    void inputPathShouldBeInvalidIfItIsADirectory() throws IOException {
        final String path = temporaryDirectory("inp").toString();
        final Validation<String, Path> verdict = CliHandler.validateInputPath(path);
        assertThat(verdict, is(invalid(path + CliHandlerMessages.PATH_IS_DIRECTORY)));
    }

    @Test
    @DisplayName("output path should be invalid if it is a directory")
    public void directoryAsOutputFileTest() throws IOException {
        final String outputPath = temporaryDirectory("out").toString();
        final Validation<String, Path> verdict = CliHandler.validateOutputPath(outputPath);
        assertThat(verdict, is(invalid(outputPath + CliHandlerMessages.PATH_IS_DIRECTORY)));
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    @DisplayName("message should be shown when input file is not readable")
    public void notReadableInputFileTest() throws IOException {
        final String inputPath = temporaryFile(
                "inaccessible",
                ".fasta",
                asFileAttribute(fromString("---------"))
        ).toString();
        final Validation<String, Path> verdict = CliHandler.validateInputPath(inputPath);
        assertThat(verdict, is(invalid(inputPath + CliHandlerMessages.FILE_IS_NOT_READABLE)));
    }

    @Test
    @DisplayName("input path should be invalid if it doesn't exist")
    public void inputFileDoesNotExistTest() {
        final String nonexistentPath = "SomeNonexistentInputFile.fasta";
        final Validation<String, Path> verdict = CliHandler.validateInputPath(nonexistentPath);
        assertThat(verdict,
                is(invalid(nonexistentPath + CliHandlerMessages.FILE_DOES_NOT_EXIST)));
    }

    @Test
    @DisplayName("message should be shown when output file already exists")
    public void outputFileAlreadyExistTest() throws IOException {
        final String outputPath = temporaryFile("out", ".fasta").toString();
        final Validation<String, Path> verdict = CliHandler.validateOutputPath(outputPath);
        assertThat(verdict, is(invalid(outputPath + CliHandlerMessages.FILE_ALREADY_EXIST)));
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    @DisplayName("message should be shown when output file is not writable")
    public void notWritableOutputFileTest() throws IOException {
        final Path inaccessible = temporaryDirectory("inaccessible", asFileAttribute(fromString("---------")));
        final String outputPath = inaccessible.resolve("out.fasta").toString();
        final Validation<String, Path> verdict = CliHandler.validateOutputPath(outputPath);
        assertThat(verdict, is(invalid(outputPath + CliHandlerMessages.FILE_IS_NOT_WRITABLE)));
    }
}
