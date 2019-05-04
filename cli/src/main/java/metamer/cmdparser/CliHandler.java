package metamer.cmdparser;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import metamer.assembler.Assembler;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;


import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.API.run;
import static io.vavr.Patterns.$Left;
import static io.vavr.Patterns.$Right;
import static io.vavr.control.Validation.combine;
import static io.vavr.control.Validation.invalid;
import static io.vavr.control.Validation.valid;
import static java.util.function.Predicate.not;

public class CliHandler {

    private static Options options = new Options()
            .addOption("h", "help", false, "Present help")
            .addOption("k", true, "Length of k mer in De Bruijn graph")
            .addOption("f", "format", true, "Format of input data: fasta or fastq")
            .addOption("i", "input", true, "Input file with reads to be analyzed")
            .addOption("o", "output", true, "Output file to write result to");

    enum Format {
        FASTA, FASTQ
    }

    public static void main(final String... args) {
        try {
            Match(parse(args)).of(
                    Case($Left($()), messages -> run(() -> messages.forEach(System.out::println))),
                    Case($Right($()), assembler -> run(assembler::assemble))
            );
        } catch (final ParseException exp) {
            System.out.println(CliHandlerMessages.NO_ARGUMENT + exp.getMessage());
        }
    }

    public static Either<Seq<String>, Assembler> parse(final String[] args) throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, args);

        if (args.length == 0 || line.hasOption("help")) {
            final List<String> usage = List.of(printHelp(options));
            return Either.left(usage);
        }

        final Validation<Seq<String>, Assembler> validationAssembler = validate(
                line.getOptionValue("input"),
                line.getOptionValue("output"),
                line.getOptionValue("k"),
                line.getOptionValue("format")
        );

        return validationAssembler.toEither();
    }

    public static Validation<Seq<String>, Assembler> validate(
            final String input,
            final String output,
            final String kValue,
            final String formatValue) {
        return combine(
                validateInputPath(input),
                validateOutputPath(output),
                validateK(kValue),
                validateFormat(formatValue)
        ).ap((source, target, k, format) -> new Assembler(source, target, k));
    }

    public static Validation<String, Path> validateInputPath(final String path) {
        return path == null
                ? invalid(CliHandlerMessages.NO_FILE_PATH)
                : Match(Paths.get(path).toFile()).of(
                Case($(not(File::exists)), () -> invalid(path + CliHandlerMessages.FILE_DOES_NOT_EXIST)),
                Case($(not(File::canRead)), () -> invalid(path + CliHandlerMessages.FILE_IS_NOT_READABLE)),
                Case($(File::isDirectory), () -> invalid(path + CliHandlerMessages.PATH_IS_DIRECTORY)),
                Case($(), file -> valid(file.toPath()))
        );
    }

    public static Validation<String, Path> validateOutputPath(final String path) {
        final Predicate<File> canWrite = file -> file.toPath().getParent().toFile().canWrite();
        return path == null
                ? invalid(CliHandlerMessages.NO_FILE_PATH)
                : Match(Paths.get(path).toFile()).of(
                Case($(File::isDirectory), () -> invalid(path + CliHandlerMessages.PATH_IS_DIRECTORY)),
                Case($(File::exists), () -> invalid(path + CliHandlerMessages.FILE_ALREADY_EXIST)),
                Case($(not(canWrite)), () -> invalid(path + CliHandlerMessages.FILE_IS_NOT_WRITABLE)),
                Case($(), file -> valid(file.toPath()))
        );
    }


    public static Validation<String, Integer> validateK(final String k) {
        return k == null
                ? invalid(CliHandlerMessages.NO_LENGTH)
                : Try.of(() -> Integer.parseInt(k)).toValid(CliHandlerMessages.INVALID_LENGTH);
    }

    public static Validation<String, Format> validateFormat(final String format) {
        return format == null
                ? invalid(CliHandlerMessages.NO_FORMAT)
                : Try.of(() -> Format.valueOf(format.toUpperCase())).toValid(CliHandlerMessages.INVALID_FORMAT);
    }

    private static String printHelp(final Options options) {
        final String commandLineSyntax = "java metamer.jar";
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(writer, 80, commandLineSyntax, null, options, 3, 5, "--- HELP ---", true);
        return stringWriter.toString();
    }
}
