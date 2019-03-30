package metamer.fastq;

import metamer.utils.ParsingException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {

    @Test
    void testIfEmptyId() {
        assertThrows(ParsingException.class, () -> Parser.formRecord(List.of(" ", "ACTGN", "+", "!!!!!")));
    }

    @Test
    void testIfIncorrectSequenceLine() {
        assertThrows(ParsingException.class, () -> Parser.formRecord(List.of("@ID", "ACTOGN", "+", "!!!!!!")));
    }

    @Test
    void testIfIncorrectQualityScoreID() {
        assertThrows(ParsingException.class, () -> Parser.formRecord(List.of("@ID", "ACTGN", "+I", "!!!!!")));
    }

    @Test
    void testIfIncorrectQualityScoreLineLength() {
        assertThrows(ParsingException.class, () -> Parser.formRecord(List.of("@ID", "ACTGN", "+", "!!!+!!")));
    }

    @Test
    void testIfQualityScoreLineContainsIncorrectSymblo() {
        assertThrows(ParsingException.class, () -> Parser.formRecord(List.of("@ID", "ACTGN", "+", "!! !!")));
    }

    @Test
    void testFormRecords() {
        Stream<String> strStream = Stream.of("@beautifulID some description", "ACGTNAA", "+", "!~#2Yu&",
                "@notSoBeautifulID", "AAACTG", "+notSoBeautifulID", "~0}[j!");
        List<Record> ourRecords = Parser.parseString(strStream).collect(Collectors.toList());

        assertThat(ourRecords, contains(
                new Record("beautifulID", "some description", "ACGTNAA", "!~#2Yu&".getBytes()),
                new Record("notSoBeautifulID","", "AAACTG", "~0}[j!".getBytes())
        ));
    }

}

