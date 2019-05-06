package metamer.fasta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class FastaTest {
    @Test
    @DisplayName("records should contain correct information when file was read")
    void testReadRecordsFromFile() {
        final Stream<String> lines = Stream.of(
                ">id1",
                "abc1",
                ">id2",
                "abc2"
        );
        final List<Record> records = Fasta.parser().read(lines).collect(toList());

        assertThat(records, contains(
                new Record(">id1", "abc1"),
                new Record(">id2", "abc2")
        ));
    }
}
