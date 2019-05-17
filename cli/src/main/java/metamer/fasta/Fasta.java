package metamer.fasta;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import metamer.fasta.exception.EmptyList;
import metamer.fasta.exception.InvalidIdentifier;
import metamer.fasta.exception.InvalidNumberOfLines;
import metamer.io.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static metamer.utils.Lists.head;

import static metamer.utils.Splitter.splitBefore;
import static metamer.utils.Streams.chunks;

public class Fasta implements Parser<Record> {
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
    public Either<Exception, Record> read(final List<String> lines) {
        if (lines.size() < 2) {
            return Either.left(new InvalidNumberOfLines(lines.size()));
        }
        final String description = lines.get(0);
        if (description.charAt(0) != '>') {
            return Either.left(new InvalidIdentifier(description.charAt(0)));
        }
        if (Option.none().equals(head(lines))) {
            return Either.left(new EmptyList());
        }

        final String uniqI;
        final String addInf;
        if (description.contains(" ")) {
            uniqI = description.substring(1, description.indexOf(' ') - 1);
            addInf = description.substring(description.indexOf(' ') + 1);
        } else {
            uniqI = description.substring(1);
            addInf = "";
        }
        final StringBuilder seq = new StringBuilder();
        for (int i = 1; i < lines.size(); i++) {
            seq.append(lines.get(i));
        }
        return Either.right(new Record(uniqI, addInf, seq.toString()));
    }

    @Override
    public Either<Exception, Seq<Record>> read(final Stream<String> lines) {
        final Stream<List<String>> chunks = chunks(splitBefore(line -> line.startsWith(IDENTIFIER_PREFIX)), lines);
        final Seq<Either<Exception, Record>> eithers = io.vavr.collection.List.ofAll(chunks.map(this::read));
        return Either.sequenceRight(eithers);
    }

    public static Fasta parser() {
        return instance;
    }
}
