package metamer.fastq;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FastQ {
    final Path path;
    final public static String FASTQ_EXTENSION = "fastq";

    public FastQ(String path) {
        this.path = Paths.get(path);
    }

    public boolean isFileValid() {
        File file = path.toFile();
        return (file.exists() && file.isFile());
    }

    public Stream<Record> records() {
        try {
            if (!metamer.utils.Paths.extension(path).equals(FASTQ_EXTENSION)) {
                throw new IOException("File is not fastQ");
            }
            if (!isFileValid()) {
                throw new IOException("FastQ file is invalid");
            }
            return Parser.parseString(Files.lines(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }
}

