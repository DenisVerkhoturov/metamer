package metamer.fastq;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;

public class FastQTest {
    @Test
    @DisplayName("records should contain correct information when file was read")
    void testReadRecordsFromFile() {
        final Either<String, Seq<Record>> records = FastQ.parser().read(Stream.of(
                "@MyCoolID some description",
                "ACTGGTCA",
                "+",
                "!~+9#hkm",
                "@MyVeryBeautifulID",
                "NATC",
                "+MyVeryBeautifulID",
                "!!!!"
        ));

        assertThat(records, instanceOf(Either.Right.class));
        assertThat(records.get(), contains(
                new Record("MyCoolID", "some description", "ACTGGTCA", "!~+9#hkm".getBytes()),
                new Record("MyVeryBeautifulID", "", "NATC", "!!!!".getBytes())
        ));
    }
}
