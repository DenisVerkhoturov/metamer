package metamer.fastq;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import metamer.io.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static metamer.utils.Lists.head;

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
    public Either<String, Seq<Record>> read(final Stream<String> lines) {
        List<String> inpStrings = lines.collect(Collectors.toList());
        List<Either<String, Record>> outList = new ArrayList<>();

        try {
            if (inpStrings.isEmpty() || inpStrings.size() % 4 != 0) {
                return Either.left("Incorrect number of input strings");
            }
            for (int i = 0; i < inpStrings.size(); i += 4) {
                outList.add(read(inpStrings.subList(i, i + 4)));
            }
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }

        final Seq<Either<String, Record>> eithers = io.vavr.collection.List.ofAll(outList);
        final Either<String, Seq<Record>> results = Either.sequenceRight(eithers);

        return results;

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
            tmpId = lines.get(0).substring(1, lines.get(0).indexOf(" ", 0));
            tmpDescription = lines.get(0).substring(lines.get(0).indexOf(" ", 0) + 1);
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

