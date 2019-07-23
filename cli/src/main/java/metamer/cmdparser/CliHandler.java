/*
 * MIT License
 *
 * Copyright (c) 2019-present Denis Verkhoturov, Aleksandra Klimina,
 * Sophia Shalgueva, Irina Shapovalova, Anna Brusnitsyna
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
import metamer.io.DecoratorGZIP;
import metamer.io.FileReader;
import metamer.io.HasSequence;
import metamer.io.Writer;
import metamer.io.FileWriter;
import metamer.io.DecoratorGZIPStd;
import metamer.io.StdOutWriter;
import metamer.io.StdInReader;
import metamer.io.Parser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementation of command line arguments parsing.
 */
public class CliHandler {

    private static boolean verbose = false;
    private static final Logger log = LogManager.getLogger(CliHandler.class.getName());
    private static final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
    private static final String logName = "metamer-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log";

    private static Options options = new Options()
            .addOption("h", "help", false, "Present help")
            .addOption("k", true, "Length of k mer in De Bruijn graph")
            .addOption("f", "format", true, "Format of input data: fasta or fastq")
            .addOption("i", "input", true, "Input file with reads to be analyzed")
            .addOption("o", "output", true, "Output file to write result to")
            .addOption("v", "verbose", false, "Present log info")
            .addOption("c", "compressed", false, "Archive using");

    public static void main(final String... args) {
        try {
            log.info("START");
            Match(parse(args)).of(
                    Case($Left($()), messages -> run(() -> messages.forEach(System.out::println))),
                    Case($Right($()), API::run)
            );
            final File logF = new File(tmpDir, "logInf.log");
            logF.renameTo(new File(tmpDir, logName));
            if (verbose) {
                System.out.println(showLog());
            }
        } catch (final Exception exp) {
            log.error("ParseException in Main 108" + exp.getMessage(), exp);
            final File userDir = new File(System.getProperty("user.dir"));
            final File logF = new File(tmpDir, logName);
            try {
                Files.copy(logF.toPath(), userDir.toPath());
            } catch (final IOException io) {
                log.error("IOException in CliHandler, Main" + io.getMessage(), io);
            }
        }
    }

    /**
     * Control function to select correct scenario.
     * <p>
     * Possible scenarios:
     * 1. Reading from file & writing to file
     * 2. Reading from file & writing to stdout
     * 3. Reading from stdin & writing to file
     * 4. Reading from stdin & writing to stdout.
     *
     * @param args Command line arguments or array of strings.
     * @return Sequence of Exceptions if there were mistakes or {@link Assembler assembler}.
     * @throws ParseException If there were some problems with files parsing.
     */
    public static Either<Seq<Exception>, Runnable> parse(final String[] args) throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, args);

        if (args.length == 0 || line.hasOption("help")) {
            return Either.right(() -> System.out.println(printHelp(options)));
        }

        final String k = line.getOptionValue("k");
        log.info("k value = " + k);
        final String format = line.getOptionValue("format");
        log.info("format value = " + format);
        final Validation<Seq<Exception>, Assembler> validation;
        final boolean compr = line.hasOption("c");
        if (line.hasOption("input") && line.hasOption("output")) {
            validation = validateFromFileToFile(
                    line.getOptionValue("input"), line.getOptionValue("output"), k, format, compr);
            log.info("strategy : from file to file, input = " + line.getOptionValue("input")
                    + ", output = " + line.getOptionValue("output"));
        } else if (line.hasOption("input")) {
            validation = validateFromFileToStd(line.getOptionValue("input"), k, format, compr);
            log.info("strategy : from file to stdout, input = " + line.getOptionValue("input"));
        } else if (line.hasOption("output")) {
            validation = validateFromStdToFile(line.getOptionValue("output"), k, format, compr);
            log.info("strategy : from stdin to file, output = " + line.getOptionValue("output"));
        } else {
            validation = validateFromStdToStd(k, format, compr);
            log.info("strategy : from stdin to stdout");
        }
        if (line.hasOption("verbose")) {
            verbose = true;
        }

        return validation.toEither().map(assembler -> assembler::assemble);
    }

    /**
     * Validation function for file to file scenario.
     *
     * @param input       Path to input file in string form.
     * @param output      Path to output file in string form.
     * @param kValue      Kmer's length in string form.
     * @param formatValue Format of input data.
     * @return object of {@link Assembler}.
     */
    public static Validation<Seq<Exception>, Assembler> validateFromFileToFile(
            final String input,
            final String output,
            final String kValue,
            final String formatValue,
            final boolean compr) {
        return combine(
                validateInputPath(input),
                validateOutputPath(output),
                validateK(kValue),
                validateFormat(formatValue)
        ).ap((source, target, k, format) -> {
            final Stream<String> reads;

            if (compr) {
                reads = new DecoratorGZIP<>(new FileReader<>(source, format.parser), source, format.parser)
                        .read().map(HasSequence::sequence);
            } else {
                reads = new FileReader<>(source, format.parser).read().map(HasSequence::sequence);
            }

            final Writer<Record> writer = new FileWriter<>(target, Fasta.parser());
            log.info("Start validation from file to file");
            return new Assembler(reads, writer::write, k);
        });
    }

    /**
     * Validation function for file to stdout scenario.
     *
     * @param input       Path to input file in string form.
     * @param kValue      Kmer's length in string form.
     * @param formatValue Format of input data.
     * @return object of {@link Assembler}.
     */

    public static Validation<Seq<Exception>, Assembler> validateFromFileToStd(
            final String input,
            final String kValue,
            final String formatValue,
            final boolean compr) {
        return combine(
                validateInputPath(input),
                validateK(kValue),
                validateFormat(formatValue)
        ).ap((source, k, format) -> {
            final Stream<String> reads;
            if (compr) {
                reads = new DecoratorGZIP<>(new FileReader<>(source, format.parser), source, format.parser)
                        .read().map(HasSequence::sequence);
            } else {
                reads = new FileReader<>(source, format.parser).read().map(HasSequence::sequence);
            }
            final Writer<Record> writer = new StdOutWriter<>(Fasta.parser());
            log.info("Start validation from file to stdout");
            return new Assembler(reads, writer::write, k);
        });
    }

    /**
     * Validation function for stdin to file scenario.
     *
     * @param output      Path to output file in string form.
     * @param kValue      Kmer's length in string form.
     * @param formatValue Format of input data.
     * @return object of {@link Assembler}.
     */
    public static Validation<Seq<Exception>, Assembler> validateFromStdToFile(
            final String output,
            final String kValue,
            final String formatValue,
            final boolean compr) {
        return combine(
                validateOutputPath(output),
                validateK(kValue),
                validateFormat(formatValue)
        ).ap((target, k, format) -> {
            final Stream<String> reads;
            final Writer<Record> writer = new FileWriter<>(target, Fasta.parser());
            if (compr) {
                reads = new DecoratorGZIPStd<>(new StdInReader<>(format.parser),
                        null, format.parser).read().map(HasSequence::sequence);
            }  else {
                reads = new StdInReader<>(format.parser).read().map(HasSequence::sequence);
            }
            log.info("Start validation from stdin to file");
            return new Assembler(reads, writer::write, k);
        });
    }

    /**
     * Validation function for stdin to stdout scenario.
     *
     * @param kValue      Kmer's length in string form.
     * @param formatValue Format of input data.
     * @return object of {@link Assembler}.
     */
    public static Validation<Seq<Exception>, Assembler> validateFromStdToStd(
            final String kValue,
            final String formatValue,
            final boolean compr) {
        return combine(validateK(kValue), validateFormat(formatValue)).ap((k, format) -> {
            final Stream<String> reads;
            if (compr) {
               reads = new DecoratorGZIPStd<>(new StdInReader<>(format.parser),
                       null, format.parser).read().map(HasSequence::sequence);
            }  else {
                reads = new StdInReader<>(format.parser).read().map(HasSequence::sequence);
            }
            final Writer<Record> writer = new StdOutWriter<>(Fasta.parser());
            log.info("Start validation from stdin to stdout");
            return new Assembler(reads, writer::write, k);
        });
    }

    /**
     * Function for validating input path.
     * <p>
     * Checks if input file is: not existent, non readable, not a directory.
     *
     * @param path Path to input file in string form.
     * @return Exception if input file isn't correct or correct path to file.
     */
    public static Validation<Exception, Path> validateInputPath(final String path) {
        final Predicate<File> canRead = file -> Files.isReadable(file.toPath());
        return Match(Paths.get(path).toFile()).of(
                Case($(not(File::exists)), () -> invalid(new NonexistentFile(Paths.get(path)))),
                Case($(not(canRead)), () -> invalid(new FileIsNotReadable(Paths.get(path)))),
                Case($(File::isDirectory), () -> invalid(new PathIsDirectory(Paths.get(path)))),
                Case($(), file -> valid(file.toPath()))
        );
    }

    /**
     * Function for validating output path.
     * <p>
     * Checks if output file is: existent, non writable, not a directory.
     *
     * @param path Path to output file in string form.
     * @return Exception if output file isn't correct or correct path to file.
     */
    public static Validation<Exception, Path> validateOutputPath(final String path) {
        final Predicate<File> canWrite = file -> Files.isWritable(file.getAbsoluteFile().toPath().getParent());
        return Match(Paths.get(path).toFile()).of(
                Case($(File::isDirectory), () -> invalid(new PathIsDirectory(Paths.get(path)))),
                Case($(File::exists), () -> invalid(new FileAlreadyExists(Paths.get(path)))),
                Case($(not(canWrite)), () -> invalid(new FileIsNotWritable(Paths.get(path)))),
                Case($(), file -> valid(file.toPath()))
        );
    }

    /**
     * Function for validating kmer's length.
     *
     * @param k Kmer's length in string form.
     * @return Exception if there length isn't correct or length as integer.
     */
    public static Validation<Exception, Integer> validateK(final String k) {
        return k == null
                ? invalid(new NoLength())
                : Try.of(() -> Integer.parseInt(k)).toValid(new InvalidLength(k));
    }

    /**
     * Function for validating format.
     *
     * @param format Format of input data in form of string.
     * @return Exception if format isn/t correct or format.
     */
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

    private static String showLog() throws IOException {
        final File file = new File(tmpDir, logName);
        return Files.readString(Paths.get(file.getAbsolutePath()));
    }

    enum Format {
        FASTA(Fasta.parser()), FASTQ(FastQ.parser());

        public final Parser<? extends HasSequence> parser;

        Format(final Parser<? extends HasSequence> parser) {
            this.parser = parser;
        }
    }
}
