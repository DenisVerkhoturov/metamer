package metamer.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileWriter<T> implements Writer<T> {
    private Path path;
    private Parser<T> parser;

    public Parser<T> parser() {
        return this.parser;
    }

    public Path path() {
        return this.path;
    }

    public FileWriter(final Path path, final Parser<T> parser) {
        this.path = path;
        this.parser = parser;
    }

    public void write(Stream<T> records) {
        final Stream<String> lines = this.parser().show(records);
        final File file = path.toFile();
        try {
            if (!file.exists() || !file.isFile())  {
                throw new IOException("Problems with file");
            }
            file.createNewFile();
            java.io.FileWriter fileWriter = new java.io.FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            lines.forEach((line) -> {
                try {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bufferedWriter.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
