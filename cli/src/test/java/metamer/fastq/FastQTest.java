package metamer.fastq;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class FastQTest {
    @Test
    @DisplayName("records should contain correct information when file was read")
    void testReadRecordsFromFile() {
        final Stream<String> lines = Stream.of(
                "@MyCoolID some description",
                "ACTGGTCA",
                "+",
                "!~+9#hkm",
                "@MyVeryBeautifulID",
                "NATC",
                "+MyVeryBeautifulID",
                "!!!!"
        );
        final List<Record> records = FastQ.parser().read(lines).collect(toList());

        assertThat(records, contains(
                new Record("MyCoolID", "some description", "ACTGGTCA", "!~+9#hkm".getBytes()),
                new Record("MyVeryBeautifulID", "", "NATC", "!!!!".getBytes())
        ));
    }
}
