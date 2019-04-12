package metamer.fastq;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static metamer.utils.Paths.resource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FastQTest {

    @Test
    void testIfFileDoesNotExist() {
        assertThrows(Exception.class, () -> resource(this.getClass(), "nofile.fastq").toString());
    }

    @Test
    void testIfFileIsNotFastQ() throws URISyntaxException {
        Path filepath = resource(this.getClass(), "nofastqfile.fast");
        assertThat(metamer.utils.Paths.extension(filepath).equals(FastQ.FASTQ_EXTENSION), is(false));
    }

    @Test
    void testReadRecordsFromFile() throws URISyntaxException {
        final FastQ file = new FastQ(resource(this.getClass(), "test.fastq"));
        final List<Record> records = file.records().collect(toList());

        assertThat(records, contains(
                new Record("MyCoolID", "some description", "ACTGGTCA", "!~+9#hkm".getBytes()),
                new Record("MyVeryBeautifulID", "", "NATC", "!!!!".getBytes())
        ));
    }

}

