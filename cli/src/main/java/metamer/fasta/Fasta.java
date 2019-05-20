/*
 * MIT License
 *
 * Copyright (c) 2019-present Denis Verkhoturov, Aleksandra Klimina,
 * Sophia Shalgueva, Irina Shapovalova, Anna Brusnitsyna
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
