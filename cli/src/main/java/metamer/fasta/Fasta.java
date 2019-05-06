package metamer.fasta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import metamer.io.Parser;
import metamer.utils.Splitter;
import metamer.utils.Streams;

public class Fasta implements Parser<Record> {

    public static final String FASTA_EXTENSION = "fasta";
    private static final String IDENTIFIER_PREFIX = ">";
    private final int LINE_LENGTH = 80;

    private final static Fasta instance = new Fasta();

    private Fasta() {
    }

    @Override
    public Stream<String> show(final Record record) {
        List<String> sequences = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        builder.append(">").append(record.uniqueIdentifier);
        if (!("".equals(record.additionalInformation))) {
            builder.append(" ").append(record.additionalInformation);
        }
        sequences.add(builder.toString());

        for (int index = 0; index < record.sequence.length(); index += LINE_LENGTH) {
            if (record.sequence.length() - index >= LINE_LENGTH) {
                sequences.add(record.sequence.substring(index, index + LINE_LENGTH));
            } else {
                sequences.add(record.sequence.substring(index));
            }
        }

        return sequences.stream();
    }

    @Override
    public Stream<String> show(final Stream<Record> records) {
        return records.flatMap(this::show);
    }

    @Override
    public Record read(final List<String> lines) {
        String uniqI;
        String addInf;
        if (lines.get(0).indexOf(' ') != -1) {
            uniqI = lines.get(0).substring(1, lines.get(0).indexOf(' ') - 1);
            addInf = lines.get(0).substring(lines.get(0).indexOf(' ') + 1);
        } else {
            uniqI = lines.get(0);
            addInf = "";
        }
        StringBuilder seq = new StringBuilder();
        for (int i = 1; i < lines.size(); i++) {
            seq.append(lines.get(i));
        }
        return new Record(uniqI, addInf, seq.toString());
    }

    @Override
    public Stream<Record> read(final Path inputPath) throws IOException {
        final Stream<String> lines = Files.lines(inputPath);
        final Stream<List<String>> chunks = Streams.chunks(Splitter.splitBefore(IDENTIFIER_PREFIX), lines);
        final Stream<Record> records = chunks.map(this::read);
        return  records;
    }

    public static Fasta parser() {
        return instance;
    }
}
