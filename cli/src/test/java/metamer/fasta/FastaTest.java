package metamer.fasta;

import metamer.io.FileReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static metamer.utils.Paths.resource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.contains;

import static metamer.utils.Paths.extension;
import static metamer.utils.Strings.multiline;

public class FastaTest {
    private final String content = multiline(">id1",
            "abc1",
            ">id2",
            "abc2");

    @Test
    @DisplayName("path should not have fasta extension when parse file with not fasta extension")
    public void testNotFastaFile() throws URISyntaxException {
        Path path = resource(this.getClass(), "out.txt");
        assertThat(extension(path), is(not(Fasta.FASTA_EXTENSION)));
    }

    @Test
    @DisplayName("path should not have fasta extension when parse file without extension")
    public void testNotFile() {
        Path path = Paths.get("notExistingFileName");
        assertThat(extension(path), is(not(Fasta.FASTA_EXTENSION)));
    }

    @Test
    @DisplayName("records should contain correct information when file was read")
    void testReadRecordsFromFile() throws IOException {
        final Path path = Files.createTempFile("fasta", "file");
        path.toFile().deleteOnExit();
        Files.write(path, content.getBytes());

        FileReader<Record> f = new FileReader<>(path, Fasta.parser());
        final List<Record> records = f.read().collect(toList());

        assertThat(records, contains(
                new Record(">id1", "abc1"),
                new Record(">id2", "abc2")
        ));
    }
}
