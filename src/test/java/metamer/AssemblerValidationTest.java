package metamer;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import metamer.assembler.Assembler;
import metamer.assembler.AssemblerValidation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static io.vavr.control.Validation.invalid;
import static io.vavr.control.Validation.valid;
import static java.nio.file.Files.createTempDirectory;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.attribute.PosixFilePermissions.asFileAttribute;
import static java.nio.file.attribute.PosixFilePermissions.fromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class AssemblerValidationTest {
    private static Path nonExisting, directory, inaccessible, accessible;

    @BeforeAll
    static void setUp() throws IOException {
        nonExisting = Paths.get("/definitely/not/existing/path/to.file");
        directory = createTempDirectory("directory");
        inaccessible = createTempFile("inaccessible", "", asFileAttribute(fromString("---------")));
        accessible = createTempFile("accessible", "");
    }

    @Test
    @DisplayName("Input path should be invalid if doesn't exist")
    void inputPathShouldBeInvalidIfDoesNotExist() {
        final Validation<String, Path> validationVerdict = AssemblerValidation.validateInputPath(nonExisting);
        assertThat(validationVerdict, is(invalid("Input path does not exist.")));
    }

    @Test
    @DisplayName("Input path should be invalid if it is a directory")
    void inputPathShouldBeInvalidIfItIsADirectory() throws IOException {
        final Validation<String, Path> validationVerdict = AssemblerValidation.validateInputPath(directory);
        assertThat(validationVerdict, is(invalid("Input path is a directory.")));
    }

    @Test
    @DisplayName("Input path should be invalid if user has no permission to read it")
    void inputPathShouldBeInvalidIfUserHasNoPermissionToReadIt() {
        final Validation<String, Path> validationVerdict = AssemblerValidation.validateInputPath(inaccessible);
        assertThat(validationVerdict, is(invalid("Input path cannot be read.")));
    }

    @Test
    @DisplayName("Input path should be valid if it exists, is not a directory and user has a permission to read it")
    void inputPathShouldBeValidIfItExistsAndIsNotADirectoryAndUserHasAPermissionToReadIt() {
        final Validation<String, Path> validationVerdict = AssemblerValidation.validateInputPath(accessible);
        assertThat(validationVerdict, is(valid(accessible)));
    }

    /*
     * Pretty much the same test cases as above but written in table-driven approach.
     * Take into consideration the way it is shorter and more scalable in terms of possible cases to test.
     *
     * In addition, to be able to use @ParameterizedTest annotation we need to define `junit-jupiter-params`
     * as a dependency.
     */

    @ParameterizedTest(name = "{index} path = {0}, expected = {1}")
    @MethodSource("paths")
    @DisplayName("Input path validation should filter out incorrect inputs")
    void inputPathValidationShouldFilterOutIncorrectInputs(final Path path, final Validation<String, Path> expected) {
        final Validation<String, Path> validationVerdict = AssemblerValidation.validateInputPath(path);
        assertThat(validationVerdict, equalTo(expected));
    }

    @SuppressWarnings({ "LineLength", "ParenPad" })
    static Stream<Arguments> paths() {
        return Stream.of(
                arguments(nonExisting , invalid("Input path does not exist.")),
                arguments(directory   , invalid("Input path is a directory.")),
                arguments(inaccessible, invalid("Input path cannot be read.")),
                arguments(accessible  , valid(accessible)                    )
        );
    }

    /*
     * Its advantages become more obvious when the amount of possible arguments combinations we need to cover grows.
     */

    @ParameterizedTest(name = "{index} input = {0}, output = {1}")
    @MethodSource("assemblerPaths")
    @DisplayName("Assembler validation should filter out incorrect paths")
    void bla(final Path input, final Path output, final Validation<Seq<String>, Assembler> expected) {
        final Validation<Seq<String>, Assembler> validationVerdict = AssemblerValidation.validate(input, output);
        assertThat(validationVerdict, equalTo(expected));
    }

    @SuppressWarnings({ "LineLength", "ParenPad" })
    static Stream<Arguments> assemblerPaths() {
        return Stream.of(
                arguments(nonExisting , nonExisting , invalid(List.of("Input path does not exist."))                               ),
                arguments(nonExisting , directory   , invalid(List.of("Input path does not exist.", "Output path already exists."))),
                arguments(nonExisting , inaccessible, invalid(List.of("Input path does not exist.", "Output path already exists."))),
                arguments(nonExisting , accessible  , invalid(List.of("Input path does not exist.", "Output path already exists."))),
                arguments(directory   , nonExisting , invalid(List.of("Input path is a directory."))                               ),
                arguments(directory   , directory   , invalid(List.of("Input path is a directory.", "Output path already exists."))),
                arguments(directory   , inaccessible, invalid(List.of("Input path is a directory.", "Output path already exists."))),
                arguments(directory   , accessible  , invalid(List.of("Input path is a directory.", "Output path already exists."))),
                arguments(inaccessible, nonExisting , invalid(List.of("Input path cannot be read."))                               ),
                arguments(inaccessible, directory   , invalid(List.of("Input path cannot be read.", "Output path already exists."))),
                arguments(inaccessible, inaccessible, invalid(List.of("Input path cannot be read.", "Output path already exists."))),
                arguments(inaccessible, accessible  , invalid(List.of("Input path cannot be read.", "Output path already exists."))),
                arguments(accessible  , nonExisting , valid(new Assembler(accessible, nonExisting))                                ),
                arguments(accessible  , directory   , invalid(List.of("Output path already exists."))                              ),
                arguments(accessible  , inaccessible, invalid(List.of("Output path already exists."))                              ),
                arguments(accessible  , accessible  , invalid(List.of("Output path already exists."))                              )
        );
    }
}
