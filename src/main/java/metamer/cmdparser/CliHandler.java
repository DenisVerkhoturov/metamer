package metamer.cmdparser;

import metamer.fastq.FastQ;
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

    public static Path getFilename() {
        return filename;
    }

    public static String getCommand() {
        return command;
    }

    public static void setFilename(Path filename) {
        CliHandler.filename = filename;
    }

    public static void setCommand(String command) {
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

    public static void parse(String[] args) throws ParseException, IOException {

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
                if ("read".equals(command)) {
                    FastQ fastQFile = new FastQ(filename.toString());
                    fastQFile.records();
                }
            }
        }

    }


    public static void main(final String... args) {
        try {
            parse(args);
        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        } catch (IOException ioExp) {
            System.out.println("File doesn't exist");
        }
    }
}
