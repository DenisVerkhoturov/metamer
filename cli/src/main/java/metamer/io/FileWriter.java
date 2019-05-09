package metamer.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileWriter<T> implements Writer<T> {
    private Path path;
    private Parser<T> parser;

    public FileWriter(final Path path, final Parser<T> parser) {
        this.path = path;
        this.parser = parser;
    }

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

    public Parser<T> parser() {
        return this.parser;
    }

    public String id() {
        return this.path.toString();
    }

}
