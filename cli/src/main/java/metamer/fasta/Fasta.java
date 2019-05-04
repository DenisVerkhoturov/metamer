package metamer.fasta;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import metamer.io.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static metamer.utils.Lists.head;

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
    public Either<String, Record> read(final List<String> lines) {
        if (lines.size() < 2) {
            return Either.left("We need more than two lines");
        }
        if (lines.get(0).charAt(0) != '>') {
            return Either.left("First string is not a indificator");
        }
        if (Option.none().equals(head(lines))) {
            return Either.left("Empty list");
        }

        String uniqI = lines.get(0).substring(1, lines.get(0).indexOf(' ') - 1);
        String addInf = lines.get(0).substring(lines.get(0).indexOf(' ') + 1);
        StringBuilder seq = new StringBuilder();
        for (int i = 1; i < lines.size(); i++) {
            seq.append(lines.get(i));
        }
        return Either.right(new Record(uniqI, addInf, seq.toString()));
    }

    @Override
    public Either<String, Seq<Record>> read(final Stream<String> inpStream) {
        List<List<String>> list = new ArrayList<>();
        int counter = -1;

        for (final String str : inpStream.collect(Collectors.toList())) {
            if (str.startsWith(IDENTIFIER_PREFIX)) {
                list.add(new ArrayList<>());
                counter++;
            }
            list.get(counter).add(str);
        }
        List<Either<String, Record>> out = new ArrayList<>();
        for (final List<String> strings : list) {
            out.add(read(strings));
        }

        final Seq<Either<String, Record>> eithers = io.vavr.collection.List.ofAll(out);
        final Either<String, Seq<Record>> results = Either.sequenceRight(eithers);

        return results;

    }

    public static Fasta parser() {
        return instance;
    }
}
