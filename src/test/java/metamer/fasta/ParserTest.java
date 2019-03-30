package metamer.fasta;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ParserTest {
    @Test
    public void testNumberOfLines() {
        final String uniqueIdentifier = "MCHU";
        final String additionalInformation = "- Calmodulin - Human, rabbit, bovine, rat, and chicken";
        final String sequence =
                "ADQLTEEQIAEFKEAFSLFDKDGDGTITTKELGTVMRSLGQNPTEAELQDMINEVDADGNGTID" +
                "FPEFLTMMARKMKDTDSEEEIREAFRVFDKDGNGYISAAELRHVMTNLGEKLTDEEVDEMIREA" +
                "DIDGDGQVNYEEFVQMMTAK";
        Record record = new Record(uniqueIdentifier, additionalInformation, sequence);
        Stream<Record> stream = Stream.of(record, record, record);

        final int expectedNumberOfLines = 3;

        assertThat((int)(new Parser().parseRecords(stream).count()), is(expectedNumberOfLines * 3));
    }

    @Test
    public void testCorrectFullInformationTransformations() {
        final String uniqueIdentifier = "MCHU";
        final String additionalInformation = "- Calmodulin - Human, rabbit, bovine, rat, and chicken";
        final String sequence =
                "ADQLTEEQIAEFKEAFSLFDKDGDGTITTKELGTVMRSLGQNPTEAELQDMINEVDADGNGTID" +
                        "FPEFLTMMARKMKDTDSEEEIREAFRVFDKDGNGYISAAELRHVMTNLGEKLTDEEVDEMIREA" +
                        "DIDGDGQVNYEEFVQMMTAK";
        Record record = new Record(uniqueIdentifier, additionalInformation, sequence);
        Stream<Record> stream = Stream.of(record);

        final String expectedString = ">" + record.uniqueIdentifier + " " + record.additionalInformation;

        assertThat(new Parser().parseRecords(stream).findFirst(), is(Optional.of(expectedString)));
    }

    @Test
    public void testCorrectShortInformationTransformations() {
        final String uniqueIdentifier = "MCHU";
        final String sequence =
                "ADQLTEEQIAEFKEAFSLFDKDGDGTITTKELGTVMRSLGQNPTEAELQDMINEVDADGNGTID" +
                        "FPEFLTMMARKMKDTDSEEEIREAFRVFDKDGNGYISAAELRHVMTNLGEKLTDEEVDEMIREA" +
                        "DIDGDGQVNYEEFVQMMTAK";
        Record record = new Record(uniqueIdentifier, sequence);
        Stream<Record> stream = Stream.of(record);

        final String expectedString = ">" + record.uniqueIdentifier;

        assertThat(new Parser().parseRecords(stream).findFirst(), is(Optional.of(expectedString)));
    }
}
