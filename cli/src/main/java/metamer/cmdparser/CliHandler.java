package metamer.cmdparser;

import io.vavr.API;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import metamer.assembler.Assembler;
import metamer.fasta.Fasta;
import metamer.fasta.Record;
import metamer.fastq.FastQ;
import metamer.io.FileReader;
import metamer.io.FileWriter;
import metamer.io.HasSequence;
import metamer.io.Parser;
import metamer.io.StdInReader;
import metamer.io.StdOutWriter;
import metamer.io.Writer;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Stream;

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

import metamer.cmdparser.exception.PathIsDirectory;
import metamer.cmdparser.exception.NonexistentFile;
import metamer.cmdparser.exception.InvalidFormat;
import metamer.cmdparser.exception.NoLength;
import metamer.cmdparser.exception.NoFormat;
import metamer.cmdparser.exception.FileIsNotWritable;
import metamer.cmdparser.exception.FileIsNotReadable;
import metamer.cmdparser.exception.FileAlreadyExists;
import metamer.cmdparser.exception.InvalidLength;

public class CliHandler {

    private static Options options = new Options()
            .addOption("h", "help", false, "Present help")
            .addOption("k", true, "Length of k mer in De Bruijn graph")
            .addOption("f", "format", true, "Format of input data: fasta or fastq")
            .addOption("i", "input", true, "Input file with reads to be analyzed")
            .addOption("o", "output", true, "Output file to write result to");

    public static void main(final String... args) {
        try {
            Match(parse(args)).of(
                    Case($Left($()), messages -> run(() -> messages.forEach(System.out::println))),
                    Case($Right($()), API::run)
            );
        } catch (final ParseException exp) {
            System.out.println(exp.getMessage());
        }
    }

    public static Either<Seq<Exception>, Runnable> parse(final String[] args) throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, args);

        if (args.length == 0 || line.hasOption("help")) {
            return Either.right(() -> System.out.println(printHelp(options)));
        }

        final String k = line.getOptionValue("k");
        final String format = line.getOptionValue("format");
        final Validation<Seq<Exception>, Assembler> validation;
        if (line.hasOption("input") && line.hasOption("output")) {
            validation = validateFromFileToFile(
                    line.getOptionValue("input"), line.getOptionValue("output"), k, format);
        } else if (line.hasOption("input")) {
            validation = validateFromFileToStd(line.getOptionValue("input"), k, format);
        } else if (line.hasOption("output")) {
            validation = validateFromStdToFile(line.getOptionValue("output"), k, format);
        } else {
            validation = validateFromStdToStd(k, format);
        }

        return validation.toEither().map(assembler -> assembler::assemble);
    }

    public static Validation<Seq<Exception>, Assembler> validateFromFileToFile(
            final String input,
            final String output,
            final String kValue,
            final String formatValue) {
        return combine(
                validateInputPath(input),
                validateOutputPath(output),
                validateK(kValue),
                validateFormat(formatValue)
        ).ap((source, target, k, format) -> {
            final Stream<String> reads = new FileReader<>(source, format.parser).read().map(HasSequence::sequence);
            final Writer<Record> writer = new FileWriter<>(target, Fasta.parser());
            return new Assembler(reads, writer::write, k);
        });
    }

    public static Validation<Seq<Exception>, Assembler> validateFromFileToStd(
            final String input,
            final String kValue,
            final String formatValue) {
        return combine(
                validateInputPath(input),
                validateK(kValue),
                validateFormat(formatValue)
        ).ap((source, k, format) -> {
            final Stream<String> reads = new FileReader<>(source, format.parser).read().map(HasSequence::sequence);
            final Writer<Record> writer = new StdOutWriter<>(Fasta.parser());
            return new Assembler(reads, writer::write, k);
        });
    }

    public static Validation<Seq<Exception>, Assembler> validateFromStdToFile(
            final String output,
            final String kValue,
            final String formatValue) {
        return combine(
                validateOutputPath(output),
                validateK(kValue),
                validateFormat(formatValue)
        ).ap((target, k, format) -> {
            final Stream<String> reads = new StdInReader<>(format.parser).read().map(HasSequence::sequence);
            final Writer<Record> writer = new FileWriter<>(target, Fasta.parser());
            return new Assembler(reads, writer::write, k);
        });
    }

    public static Validation<Seq<Exception>, Assembler> validateFromStdToStd(
            final String kValue,
            final String formatValue) {
        return combine(validateK(kValue), validateFormat(formatValue)).ap((k, format) -> {
            final Stream<String> reads = new StdInReader<>(format.parser).read().map(HasSequence::sequence);
            final Writer<Record> writer = new StdOutWriter<>(Fasta.parser());
            return new Assembler(reads, writer::write, k);
        });
    }

    public static Validation<Exception, Path> validateInputPath(final String path) {
        final Predicate<File> canRead = file -> Files.isReadable(file.toPath());
        return Match(Paths.get(path).toFile()).of(
                Case($(not(File::exists)), () -> invalid(new NonexistentFile(Paths.get(path)))),
                Case($(not(canRead)), () -> invalid(new FileIsNotReadable(Paths.get(path)))),
                Case($(File::isDirectory), () -> invalid(new PathIsDirectory(Paths.get(path)))),
                Case($(), file -> valid(file.toPath()))
        );
    }

    public static Validation<Exception, Path> validateOutputPath(final String path) {
        final Predicate<File> canWrite = file -> Files.isWritable(file.toPath().getParent());
        return Match(Paths.get(path).toFile()).of(
                Case($(File::isDirectory), () -> invalid(new PathIsDirectory(Paths.get(path)))),
                Case($(File::exists), () -> invalid(new FileAlreadyExists(Paths.get(path)))),
                Case($(not(canWrite)), () -> invalid(new FileIsNotWritable(Paths.get(path)))),
                Case($(), file -> valid(file.toPath()))
        );
    }

    public static Validation<Exception, Integer> validateK(final String k) {
        return k == null
                ? invalid(new NoLength())
                : Try.of(() -> Integer.parseInt(k)).toValid(new InvalidLength(k));
    }

    public static Validation<Exception, Format> validateFormat(final String format) {
        return format == null
                ? invalid(new NoFormat())
                : Try.of(() -> Format.valueOf(format.toUpperCase())).toValid(new InvalidFormat(format));
    }

    private static String printHelp(final Options options) {
        final String commandLineSyntax = "java metamer.jar";
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(writer, 80, commandLineSyntax, null, options, 3, 5, "--- HELP ---", true);
        return stringWriter.toString();
    }

    enum Format {
        FASTA(Fasta.parser()), FASTQ(FastQ.parser());

        public final Parser<? extends HasSequence> parser;

        Format(final Parser<? extends HasSequence> parser) {
            this.parser = parser;
        }
    }
}
