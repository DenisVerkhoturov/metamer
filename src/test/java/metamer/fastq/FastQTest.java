package metamer.fastq;

import metamer.io.FileReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static metamer.utils.Paths.resource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FastQTest {

    @Test
    @DisplayName("exception should be thrown when file doesn't exist")
    void testIfFileDoesNotExist() {
        assertThrows(Exception.class, () -> resource(this.getClass(), "nofile.fastq").toString());
    }

    @Test
    @DisplayName("path should not have fastq extension when parse file with non fastq extension")
    void testIfFileIsNotFastQ() throws URISyntaxException {
        Path filepath = resource(this.getClass(), "nofastqfile.fast");
        assertThat(metamer.utils.Paths.extension(filepath).equals(FastQ.FASTQ_EXTENSION), is(false));
    }

    @Test
    @DisplayName("records should contain correct information when file was read")
    void testReadRecordsFromFile() throws URISyntaxException {
        FileReader<Record> f = new FileReader<>(resource(this.getClass(), "test.fastq"),
                FastQ.parser());
        final List<Record> records = f.read().collect(toList());

        assertThat(records, contains(
                new Record("MyCoolID", "some description", "ACTGGTCA", "!~+9#hkm".getBytes()),
                new Record("MyVeryBeautifulID", "", "NATC", "!!!!".getBytes())
        ));
    }

}

