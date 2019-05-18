package metamer.cmdparser;

import io.vavr.control.Validation;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import metamer.cmdparser.exception.PathIsDirectory;
import metamer.cmdparser.exception.NonexistentFile;
import metamer.cmdparser.exception.InvalidFormat;
import metamer.cmdparser.exception.FileIsNotWritable;
import metamer.cmdparser.exception.FileIsNotReadable;
import metamer.cmdparser.exception.FileAlreadyExists;
import metamer.cmdparser.exception.InvalidLength;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.nio.file.Paths;

import static io.vavr.control.Validation.invalid;
import static metamer.utils.CliHandlerUtils.temporaryDirectory;
import static metamer.utils.CliHandlerUtils.temporaryFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CliHandlerTest {

    private static Path inaccessible(final Path path) throws IOException {
        final String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            final UserPrincipal currentUser =
                    path
                            .getFileSystem()
                            .getUserPrincipalLookupService()
                            .lookupPrincipalByName(System.getProperty("user.name"));
            final AclFileAttributeView view = Files.getFileAttributeView(path, AclFileAttributeView.class);
            final AclEntry denyReadAndWrite =
                    AclEntry
                            .newBuilder()
                            .setType(AclEntryType.DENY)
                            .setPrincipal(currentUser)
                            .setPermissions(AclEntryPermission.READ_DATA, AclEntryPermission.ADD_FILE)
                            .build();

            final List<AclEntry> acl = view.getAcl();
            acl.add(0, denyReadAndWrite);
            view.setAcl(acl);
        } else {
            path.toFile().setReadable(false);
            path.toFile().setWritable(false);
        }
        return path;
    }

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
        final Validation<Exception, Integer> verdict = CliHandler.validateK("abc");
        assertThat(verdict, is(invalid(new InvalidLength("abc"))));
    }

    @Test
    @DisplayName("message should be shown when key's -f argument is not fasta or fastq")
    public void invalidFormatTest() {
        final Validation<Exception, CliHandler.Format> verdict = CliHandler.validateFormat("fast");
        assertThat(verdict, is(invalid(new InvalidFormat("fast"))));
    }

    @Test
    @DisplayName("input path should be invalid if it is a directory")
    void inputPathShouldBeInvalidIfItIsADirectory() throws IOException {
        final String path = temporaryDirectory("inp").toString();
        final Validation<Exception, Path> verdict = CliHandler.validateInputPath(path);
        assertThat(verdict, is(invalid(new PathIsDirectory(Paths.get(path)))));
    }

    @Test
    @DisplayName("output path should be invalid if it is a directory")
    public void directoryAsOutputFileTest() throws IOException {
        final String outputPath = temporaryDirectory("out").toString();
        final Validation<Exception, Path> verdict = CliHandler.validateOutputPath(outputPath);
        assertThat(verdict, is(invalid(new PathIsDirectory(Paths.get(outputPath)))));
    }

    @Test
    @DisplayName("message should be shown when input file is not readable")
    public void notReadableInputFileTest() throws IOException {
        final String inputPath = inaccessible(temporaryFile("inaccessible", ".fasta")).toString();
        final Validation<Exception, Path> verdict = CliHandler.validateInputPath(inputPath);
        assertThat(verdict, is(invalid(new FileIsNotReadable(Paths.get(inputPath)))));
    }

    @Test
    @DisplayName("input path should be invalid if it doesn't exist")
    public void inputFileDoesNotExistTest() {
        final String nonexistentPath = "SomeNonexistentInputFile.fasta";
        final Validation<Exception, Path> verdict = CliHandler.validateInputPath(nonexistentPath);
        assertThat(verdict,
                is(invalid(new NonexistentFile(Paths.get(nonexistentPath)))));
    }

    @Test
    @DisplayName("message should be shown when output file already exists")
    public void outputFileAlreadyExistTest() throws IOException {
        final String outputPath = temporaryFile("out", ".fasta").toString();
        final Validation<Exception, Path> verdict = CliHandler.validateOutputPath(outputPath);
        assertThat(verdict, is(invalid(new FileAlreadyExists(Paths.get(outputPath)))));
    }

    @Test
    @DisplayName("message should be shown when output file is not writable")
    public void notWritableOutputFileTest() throws IOException {
        final Path inaccessibleDirectory = inaccessible(temporaryDirectory("inaccessible"));
        final String outputPath = inaccessibleDirectory.resolve("out.fasta").toString();
        final Validation<Exception, Path> verdict = CliHandler.validateOutputPath(outputPath);
        assertThat(verdict, is(invalid(new FileIsNotWritable(Paths.get(outputPath)))));
    }
}
