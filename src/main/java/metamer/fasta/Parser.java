package metamer.fasta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    private final int LINE_LENGTH = 80;
    private static final String IDENTIFIER_PREFIX = ">";

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

    public static Stream<Record> parseString(Stream<String> inpStream) {
        List<Record> outList = new ArrayList<>();

        try {
            String tmpId = "";
            String tmpInformation = "";
            String tmpSequence = "";

            for (String str: inpStream.collect(Collectors.toList())) {
                if (str.startsWith(IDENTIFIER_PREFIX)) {
                    if (!tmpId.equals("")) {
                        outList.add(new Record(tmpId, tmpInformation, tmpSequence));
                    }
                    if (str.contains(" ")) {
                        tmpId = str.substring(1, str.indexOf(" ", 0));
                        tmpInformation = str.substring(str.indexOf(" ", 0) + 1);
                    } else {
                        tmpId = str.substring(1);
                        tmpInformation = "";
                    }
                    tmpSequence = "";
                } else {
                    tmpSequence += str;
                }
            }
            outList.add(new Record(tmpId, tmpInformation, tmpSequence));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return outList.stream();
    }
}
