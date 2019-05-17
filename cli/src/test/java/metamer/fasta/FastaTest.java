package metamer.fasta;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;

public class FastaTest {
    @Test
    @DisplayName("records should contain correct information when file was read")
    void testReadRecordsFromFile() {
        final Either<Exception, Seq<Record>> records = Fasta.parser().read(Stream.of(
                ">id1",
                "abc1",
                ">id2",
                "abc2"
        ));
        assertThat(records, instanceOf(Either.Right.class));
        assertThat(records.get(), contains(
                new Record("id1", "abc1"),
                new Record("id2", "abc2")
        ));
    }
}
