package metamer.fasta;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import static metamer.utils.Paths.extension;

public class FastaTest {
    private Path testResource(final String filename) throws URISyntaxException {
        return Paths.get(getClass().getResource(filename).toURI());
    }

    @Test
    public void testNotFastaFile() throws URISyntaxException {
        Path path = testResource("out.txt");
        assertThat(extension(path), is(not(Fasta.FASTA_EXTENSION)));
    }

    @Test
    public void testNotFile() {
        Path path = Paths.get("notExistingFileName");
        assertThat(extension(path), is(not(Fasta.FASTA_EXTENSION)));
    }

    @Test
    public void testIsFastaFile() throws URISyntaxException {
        Path path = testResource("out.fasta");
        assertThat(extension(path), is(Fasta.FASTA_EXTENSION));
    }
}
