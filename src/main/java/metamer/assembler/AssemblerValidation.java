package metamer.assembler;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.io.File;
import java.nio.file.Path;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.control.Validation.combine;
import static io.vavr.control.Validation.invalid;
import static io.vavr.control.Validation.valid;
import static java.util.Objects.requireNonNull;
import static java.util.function.Predicate.not;

public class AssemblerValidation {
    public static Validation<Seq<String>, Assembler> validate(final Path input, final Path output) {
        return combine(validateInputPath(input), validateOutputPath(output)).ap(Assembler::new);
    }

    public static Validation<String, Path> validateInputPath(final Path path) {
        final File file = requireNonNull(path).toFile();
        return Match(file).of(
                Case($(not(File::exists)), () -> invalid("Input path does not exist.")),
                Case($(not(File::canRead)), () -> invalid("Input path cannot be read.")),
                Case($(File::isDirectory), () -> invalid("Input path is a directory.")),
                Case($(), () -> valid(path))
        );
    }

    public static Validation<String, Path> validateOutputPath(final Path path) {
        final File file = requireNonNull(path).toFile();
        return Match(file).of(
                Case($(File::exists), () -> invalid("Output path already exists.")),
                Case($(File::canWrite), () -> invalid("Output path cannot be written.")),
                Case($(), () -> valid(path))
        );
    }
}
