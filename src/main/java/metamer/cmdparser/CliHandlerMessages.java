package metamer.cmdparser;

public class CliHandlerMessages {
    public static final String PATH_IS_DIRECTORY = " is a directory. Please, enter required file";
    public static final String FILE_ALREADY_EXIST = " file already exist and cannot be overwritten";
    public static final String FILE_DOES_NOT_EXIST = " file doesn't exist";
    public static final String FILE_IS_NOT_READABLE = " file is not permitted to be read";
    public static final String FILE_IS_NOT_WRITABLE = " file is not permitted to be written";

    public static final String INVALID_LENGTH = "Invalid format of length: ";
    public static final String INVALID_FORMAT = "Invalid format of file. Must be one of these: fasta or fastq";
    public static final String NO_FILE_PATH = "Invalid file path. Please, enter file path again";
    public static final String NO_LENGTH = "You must enter length of k mer. Usage: -k <length>";
    public static final String NO_FORMAT = "You must enter format of data. Usage: -f <format> or --format <format>";
    public static final String NO_ARGUMENT = "No arguments were entered: ";
}
