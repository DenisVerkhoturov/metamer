package metamer.cmdparser.exception;

public class NoFilePath extends Exception {
    public NoFilePath() {
        super("No file path was provided, a correct filepath is expected");
    }
}
