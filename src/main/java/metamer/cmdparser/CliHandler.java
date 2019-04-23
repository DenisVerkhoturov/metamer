package metamer.cmdparser;

import metamer.assembler.Assembler;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CliHandler {

    private static Path filename;
    private static String command;

    public class Messages {
        public static final String PATH_IS_DIRECTORY = " is a directory. Please, enter required file";
        public static final String FILE_ALREADY_EXIST = " file already exist and cannot be overwritten";
        public static final String FILE_DOES_NOT_EXIST = " file doesn't exist";
        public static final String FILE_IS_NOT_READABLE = " file is not permitted to be read";
        public static final String FILE_IS_NOT_WRITABLE = " file is not permitted to be written";
    }

    public static Path getFilename() {
        return filename;
    }

    public static String getCommand() {
        return command;
    }

    public static void setFilename(final Path filename) {
        CliHandler.filename = filename;
    }

    public static void setCommand(final String command) {
        CliHandler.command = command;
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

    public static void parse(final String[] args) throws ParseException, IOException {

        Options options = new Options();
        options.addOption("c", "command", true, "Which command to do");
        options.addOption("h", "help", false, "Present help");
        options.addOption("f", "filepath", true, "Path to file");

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        if (line.hasOption("help")) {
            printHelp(options, 80, "Options", "-- HELP --", 3, 5, true, System.out);
        }
        if (line.hasOption("filepath")) {
            String com = line.getOptionValue("f", null);

            if (com == null) {
                System.out.println("Wrong param for filepath option");
            } else {
                setFilename(Paths.get(com));
                File file = filename.toFile();
                if (!(file.exists() && file.isFile())) {
                    throw new IOException();
                }
                System.out.println("You enter filename " + filename);
            }
        }
        if (line.hasOption("command")) {
            String com = line.getOptionValue("c", null);

            if (com == null) {
                System.out.println("Wrong param for command option");
            } else {
                setCommand(com);
            }
        }

        if (!(line.hasOption("c") || line.hasOption("f") || line.hasOption("h"))) {
            if (args.length < 2) {
                printHelp(options, 80, "Options", "-- HELP --", 3, 5, true, System.out);
                return;
            }

            Path inpFilePath = Paths.get(args[0]);
            Path outFilePath = Paths.get(args[1]);

            if (inpFilePath.toFile().isDirectory()) {
                System.out.println(inpFilePath.toString() + Messages.PATH_IS_DIRECTORY);
                return;
            }
            if (!inpFilePath.toFile().exists()) {
                System.out.println(inpFilePath.toString() + Messages.FILE_DOES_NOT_EXIST);
                return;
            }
            if (!inpFilePath.toFile().canRead()) {
                System.out.println(inpFilePath.toString() + Messages.FILE_IS_NOT_READABLE);
                return;
            }

            if (outFilePath.toFile().isDirectory()) {
                System.out.println(outFilePath.toString() + Messages.PATH_IS_DIRECTORY);
                return;
            }
            if (outFilePath.toFile().exists()) {
                System.out.println(outFilePath.toString() + Messages.FILE_ALREADY_EXIST);
                return;
            }
            if (!outFilePath.toFile().canWrite()) {
                System.out.println(outFilePath.toString() + Messages.FILE_IS_NOT_WRITABLE);
                return;
            }
            if (!outFilePath.toFile().createNewFile()) {
                System.out.println("Что-то пошло не так");
                return;
            }

            String type = "fasta";
            if (type.equals("fasta")) {
                Assembler assembler = new Assembler(inpFilePath, outFilePath);
                assembler.assemble();
            }
        }
    }

    public static void main(final String... args) {
        try {
            parse(args);
        } catch (final ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        } catch (final IOException ioExp) {
            System.out.println("File doesn't exist");
        }
    }
}
