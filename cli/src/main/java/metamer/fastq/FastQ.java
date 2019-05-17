package metamer.fastq;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import metamer.fasta.exception.EmptyList;
import metamer.fasta.exception.InvalidIdentifier;
import metamer.fasta.exception.InvalidNumberOfLines;
import metamer.fastq.exception.InvalidQualityScoreID;
import metamer.fastq.exception.InvalidQualityScoreLine;
import metamer.fastq.exception.InvalidQualityScoreLineLength;
import metamer.fastq.exception.InvalidSequenceLine;
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
    public Either<Exception, Seq<Record>> read(final Stream<String> lines) {
        final Stream<List<String>> chunks = chunks(splitBefore(line -> line.startsWith(IDENTIFIER_PREFIX)), lines);
        final Seq<Either<Exception, Record>> eithers = io.vavr.collection.List.ofAll(chunks.map(this::read));
        return Either.sequenceRight(eithers);

    }

    @Override
    public Either<Exception, Record> read(final List<String> lines) {
        if (lines.size() < 2) {
            return Either.left(new InvalidNumberOfLines(lines.size()));
        }
        if (Option.none().equals(head(lines))) {
            return Either.left(new EmptyList());
        }
        String tmpId;
        String tmpDescription;

        if (!lines.get(0).startsWith(IDENTIFIER_PREFIX)) {
            return  Either.left(new InvalidIdentifier(lines.get(0).charAt(0)));
        }
        if (lines.get(0).contains(" ")) {
            tmpId = lines.get(0).substring(1, lines.get(0).indexOf(" "));
            tmpDescription = lines.get(0).substring(lines.get(0).indexOf(" ") + 1);
        } else {
            tmpId = lines.get(0).substring(1);
            tmpDescription = "";
        }

        if (!lines.get(1).matches("[ACGTN]+")) {
            return Either.left(new InvalidSequenceLine(lines.get(1)));
        }

        if (!lines.get(2).matches("\\+\\s*") && !lines.get(2).equals("+" + tmpId)) {
            return Either.left(new InvalidQualityScoreID(lines.get(2)));
        }

        if (lines.get(3).length() != lines.get(1).length()) {
            return Either.left(new InvalidQualityScoreLineLength(lines.get(3).length()));
        }
        if (!lines.get(3).matches("[\\!-\\~]+")) {
            return Either.left(new InvalidQualityScoreLine(lines.get(3)));
        }

        return Either.right(new Record(tmpId, tmpDescription, lines.get(1), lines.get(3).getBytes()));
    }

    public static FastQ parser() {
        return instance;
    }
}
