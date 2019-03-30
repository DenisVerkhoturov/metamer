package metamer.fastq;

import metamer.utils.ParsingException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    private static final String IDENTIFIER_PREFIX = "@";

    public static Record formRecord(List<String> strings) throws ParsingException {
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

    public static Stream<Record> parseString(Stream<String> inpStream) {
        List<String> inpStrings = inpStream.collect(Collectors.toList());
        List<Record> outList = new ArrayList<>();

        try {
            if (inpStrings.isEmpty() || inpStrings.size() % 4 != 0) {
                throw new ParsingException("Incorrect number of input strings");
            }
            for (int i = 0; i < inpStrings.size(); i += 4) {
                outList.add(formRecord(inpStrings.subList(i, i + 4)));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return outList.stream();
    }
}

