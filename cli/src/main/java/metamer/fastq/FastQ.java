package metamer.fastq;

import java.util.List;
import java.util.stream.Stream;

import metamer.io.Parser;
import metamer.utils.ParsingException;

import static metamer.utils.Splitter.splitBefore;
import static metamer.utils.Streams.chunks;

public class FastQ implements Parser<Record> {
    final public static String FASTQ_EXTENSION = "fastq";
    private static final String IDENTIFIER_PREFIX = "@";

    private static final FastQ instance = new FastQ();

    private FastQ() {
    }

    @Override
    public Stream<String> show(final Record record) {
        return null;
    }

    @Override
    public Stream<String> show(final Stream<Record> records) {
        return null;
    }

    @Override
    public Stream<Record> read(final Stream<String> lines) {
        final Stream<List<String>> chunks = chunks(splitBefore(line -> line.startsWith(IDENTIFIER_PREFIX)), lines);
        return chunks.map(this::formRecord);
    }

    @Override
    public Record read(final List<String> lines) {
        return null;
    }

    public static FastQ parser() {
        return instance;
    }

    public Record formRecord(final List<String> strings) throws ParsingException {
        String tmpId;
        String tmpDescription;

        if (!strings.get(0).startsWith(IDENTIFIER_PREFIX)) {
            throw new ParsingException("Incorrect ID line");
        }
        if (strings.get(0).contains(" ")) {
            tmpId = strings.get(0).substring(1, strings.get(0).indexOf(" ", 0));
            tmpDescription = strings.get(0).substring(strings.get(0).indexOf(" ", 0) + 1);
        } else {
            tmpId = strings.get(0).substring(1);
            tmpDescription = "";
        }

        if (!strings.get(1).matches("[ACGTN]+")) {
            throw new ParsingException("Incorrect sequence line");
        }

        if (!strings.get(2).matches("\\+\\s*") && !strings.get(2).equals("+" + tmpId)) {
            throw new ParsingException("Invalid quality score ID");
        }

        if (strings.get(3).length() != strings.get(1).length()) {
            throw new ParsingException("Incorrect quality score line length");
        }
        if (!strings.get(3).matches("[\\!-\\~]+")) {
            throw new ParsingException("Quality score line contains incorrect symbols");
        }

        return new Record(tmpId, tmpDescription, strings.get(1), strings.get(3).getBytes());
    }
}
