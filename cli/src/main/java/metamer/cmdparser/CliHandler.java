package metamer.cmdparser;

import metamer.assembler.Assembler;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CliHandler {

    private static Path inputPath;
    private static Path outputPath;
    private static Format format;
    private static IOFormat iFormat;
    private static IOFormat oFormat;
    private static int k;

    enum Format {
        FASTA, FASTQ
    }

    enum IOFormat {
        FILE, STDIN, STDOUT
    }

    private static void printHelp(
            final Options options,
            final int printedRowWidth,
            final String header,
            final String footer,
            final int spacesBeforeOption,
            final int spacesBeforeOptionDescription,
            final boolean displayUsage,
            final OutputStream out) {
        final String commandLineSyntax = "java metamer.jar";
        final PrintWriter writer = new PrintWriter(out);
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(writer, printedRowWidth, commandLineSyntax, header, options,
                spacesBeforeOption, spacesBeforeOptionDescription, footer, displayUsage);
        writer.flush();
    }

    public static void parse(final String[] args) throws ParseException, NumberFormatException {

        Options options = new Options();
        options.addOption("h", "help", false, "Present help");
        options.addOption("k", true, "Length of k mer in De Bruijn graph");
        options.addOption("f", "format", true, "Format of input data: fasta or fastq");
        options.addOption("i", "input", true, "Input file with reads to be analyzed");
        options.addOption("o", "output", true, "Output file to write result to");

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        if (args.length == 0) {
            printHelp(options, 80, "Options", "-- HELP --", 3, 5, true, System.out);
            return;
        }

        if (line.hasOption("help")) {
            printHelp(options, 80, "Options", "-- HELP --", 3, 5, true, System.out);
            return;
        }

        if (line.hasOption("k")) {
            String length = line.getOptionValue("k", null);

            if (length == null) {
                System.out.println(CliHandlerMessages.INVALID_LENGTH);
                return;
            } else {
                CliHandler.k = Integer.parseInt(length);
            }
        } else {
            System.out.println(CliHandlerMessages.NO_LENGTH);
            return;
        }

        if (line.hasOption("format")) {
            String format = line.getOptionValue("f", null);

            if (format.equals("fasta")) {
                CliHandler.format = Format.FASTA;
            } else if (format.equals("fastq")) {
                CliHandler.format = Format.FASTQ;
            } else {
                System.out.println(CliHandlerMessages.INVALID_FORMAT);
                return;
            }
        } else {
            System.out.println(CliHandlerMessages.NO_FORMAT);
            return;
        }

        if (line.hasOption("input")) {
            String inputPath = line.getOptionValue("i", null);

            if (inputPath == null) {
                System.out.println(CliHandlerMessages.NO_FILE_PATH);
                return;
            } else {
                CliHandler.inputPath = Paths.get(inputPath);
                CliHandler.iFormat = IOFormat.FILE;
            }
        } else {
            CliHandler.iFormat = IOFormat.STDIN;
        }

        if (line.hasOption("output")) {
            String outputPath = line.getOptionValue("o", null);

            if (outputPath == null) {
                System.out.println(CliHandlerMessages.NO_FILE_PATH);
                return;
            } else {
                CliHandler.outputPath = Paths.get(outputPath);
                CliHandler.oFormat = IOFormat.FILE;
            }
        } else {
            CliHandler.oFormat = IOFormat.STDOUT;
        }

        if (inputPath != null) {
            if (inputPath.toFile().isDirectory()) {
                System.out.println(inputPath.toString() + CliHandlerMessages.PATH_IS_DIRECTORY);
                return;
            }
            if (!inputPath.toFile().exists()) {
                System.out.println(inputPath.toString() + CliHandlerMessages.FILE_DOES_NOT_EXIST);
                return;
            }
            if (!inputPath.toFile().canRead()) {
                System.out.println(inputPath.toString() + CliHandlerMessages.FILE_IS_NOT_READABLE);
                return;
            }
        }

        if (outputPath != null) {
            if (outputPath.toFile().isDirectory()) {
                System.out.println(outputPath.toString() + CliHandlerMessages.PATH_IS_DIRECTORY);
                return;
            }
            if (outputPath.toFile().exists()) {
                System.out.println(outputPath.toString() + CliHandlerMessages.FILE_ALREADY_EXIST);
                return;
            }
            if (!outputPath.toFile().canWrite()) {
                System.out.println(outputPath.toString() + CliHandlerMessages.FILE_IS_NOT_WRITABLE);
                return;
            }
        }

        Assembler assembler;
        switch (format) {
            case FASTA: {
                //Assembler constructors for fasta
                if (iFormat == IOFormat.FILE && oFormat == IOFormat.FILE) {
                    assembler = new Assembler(inputPath, outputPath);
                    assembler.assemble();
                }
                if (iFormat == IOFormat.FILE && oFormat == IOFormat.STDOUT) {
                    //constructor + method assemble
                }
                if (iFormat == IOFormat.STDIN && oFormat == IOFormat.FILE) {
                    //constructor + method assemble
                }
                if (iFormat == IOFormat.STDIN && oFormat == IOFormat.STDOUT) {
                    //constructor + method assemble
                }
                break;
            }
            case FASTQ: {
                //Assembler constructors for fastq and certain i format and o format
                if (iFormat == IOFormat.FILE && oFormat == IOFormat.FILE) {
                    assembler = new Assembler(inputPath, outputPath);
                    assembler.assemble();
                }
                if (iFormat == IOFormat.FILE && oFormat == IOFormat.STDOUT) {
                    //constructor + method assemble
                }
                if (iFormat == IOFormat.STDIN && oFormat == IOFormat.FILE) {
                    //constructor + method assemble
                }
                if (iFormat == IOFormat.STDIN && oFormat == IOFormat.STDOUT) {
                    //constructor + method assemble
                }
                break;
            }
        }
    }

    public static void main(final String... args) {
        try {
            parse(args);
        } catch (final ParseException exp) {
            System.out.println(CliHandlerMessages.NO_ARGUMENT + exp.getMessage());
        } catch (final NumberFormatException numExp) {
            System.out.println(CliHandlerMessages.INVALID_FORMAT + numExp.getMessage());
        }
    }
}
