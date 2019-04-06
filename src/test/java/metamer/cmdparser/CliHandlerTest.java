package metamer.cmdparser;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CliHandlerTest {
    @Test
    public void testFilenameReading() {
        CliHandler.main("-f", "test.txt");
        assertThat(CliHandler.getFilename(), equalTo(Paths.get("test.txt")));
    }

    @Test
    public void testCommandReading() {
        CliHandler.main("-c", "print");
        assertThat(CliHandler.getCommand(), equalTo("print"));
    }

    @Test
    public void testWithFilenameAndCommand() {
        CliHandler.main("-c", "print", "-f", "test.txt");
        assertThat(CliHandler.getCommand(), equalTo("print"));
        assertThat(CliHandler.getFilename(), equalTo(Paths.get("test.txt")));
    }

    @Test
    public void longNameTest() {
        CliHandler.main("--command", "print");
        assertThat(CliHandler.getCommand(), equalTo("print"));
    }

    @Test
    public void testIfFilenameArgIsNull() {
        String[] args = {"-f"};
        Throwable thrown = assertThrows(ParseException.class, () -> {
            CliHandler.parse(args);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    public void testIfCommandArgIsNull() {
        String[] args = {"-c"};
        Throwable thrown = assertThrows(ParseException.class, () -> {
            CliHandler.parse(args);
        });
        assertNotNull(thrown.getMessage());
    }

}
