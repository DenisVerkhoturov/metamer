package metamer.fasta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Parser {
    private final int LINE_LENGTH = 80;

    public Stream<String> parseRecords(Stream<Record> stream) {
        List<String> sequences = new ArrayList<>();

        stream.forEach((record) -> {
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
        });
        return sequences.stream();
    }
}
