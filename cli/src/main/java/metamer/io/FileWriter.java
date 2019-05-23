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
package metamer.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Class for writing to file.
 *
 * @param <T> Type of Record: {@link metamer.fasta.Record} or {@link metamer.fastq.Record}.
 */
public class FileWriter<T> implements Writer<T> {
    private Path path;
    private Parser<T> parser;

    /**
     * Constructor - initializing all fields.
     *
     * @param path      Path to current file.
     * @param parser    Type of parser for correct choice for record type.
     */
    public FileWriter(final Path path, final Parser<T> parser) {
        this.path = path;
        this.parser = parser;
    }

    /**
     * Write result of workflow process into file.
     *
     * This function creates output file. That is why there mustn't be any before start or we will
     * get an exception.
     *
     * @param records Stream of records created after workflow.
     */
    public void write(final Stream<T> records) {
        final Stream<String> lines = this.parser().show(records);
        final File file = path.toFile();

        try {
            file.createNewFile();

            if (!file.exists() || !file.isFile())  {
                throw new IOException("Problems with file");
            }

            java.io.FileWriter fileWriter = new java.io.FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            lines.forEach((line) -> {
                try {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            });
            bufferedWriter.close();
            fileWriter.close();

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get current parser.
     *
     * Parser may be one of {@link metamer.fasta.Fasta} of {@link metamer.fastq.FastQ}
     *
     * @return Parser.
     */
    public Parser<T> parser() {
        return this.parser;
    }

    /**
     * Get sequence id.
     *
     * @return Id in future.
     */
    public String id() {
        return this.path.toString();
    }

}
