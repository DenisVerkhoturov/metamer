package metamer.fastq;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import metamer.io.Parser;

import java.util.List;
import java.util.stream.Stream;

import static metamer.utils.Lists.head;

import static metamer.utils.Splitter.splitBefore;
import static metamer.utils.Streams.chunks;

public class FastQ implements Parser<Record> {
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
    public Either<String, Seq<Record>> read(final Stream<String> lines) {
        final Stream<List<String>> chunks = chunks(splitBefore(line -> line.startsWith(IDENTIFIER_PREFIX)), lines);
        final Seq<Either<String, Record>> eithers = io.vavr.collection.List.ofAll(chunks.map(this::read));
        return Either.sequenceRight(eithers);

    }

    @Override
    public Either<String, Record> read(final List<String> lines) {
        if (lines.size() < 2) {
            return Either.left("We need more than two lines");
        }
        if (Option.none().equals(head(lines))) {
            return Either.left("Empty list");
        }
        String tmpId;
        String tmpDescription;

        if (!lines.get(0).startsWith(IDENTIFIER_PREFIX)) {
            return  Either.left("Incorrect ID line");
        }
        if (lines.get(0).contains(" ")) {
            tmpId = lines.get(0).substring(1, lines.get(0).indexOf(" "));
            tmpDescription = lines.get(0).substring(lines.get(0).indexOf(" ") + 1);
        } else {
            tmpId = lines.get(0).substring(1);
            tmpDescription = "";
        }

        if (!lines.get(1).matches("[ACGTN]+")) {
            return Either.left("Incorrect sequence line");
        }

        if (!lines.get(2).matches("\\+\\s*") && !lines.get(2).equals("+" + tmpId)) {
            return Either.left("Invalid quality score ID");
        }

        if (lines.get(3).length() != lines.get(1).length()) {
            return Either.left("Incorrect quality score line length");
        }
        if (!lines.get(3).matches("[\\!-\\~]+")) {
            return Either.left("Quality score line contains incorrect symbols");
        }

        return Either.right(new Record(tmpId, tmpDescription, lines.get(1), lines.get(3).getBytes()));
    }

    public static FastQ parser() {
        return instance;
    }
}
