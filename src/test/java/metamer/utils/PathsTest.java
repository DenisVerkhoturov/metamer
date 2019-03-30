package metamer.utils;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static metamer.utils.Paths.extension;

public class PathsTest {
    @Test
    public void testDoublePointInFileName() {
        Path path = Paths.get("/test/test.tar.gz");
        assertThat(extension(path), is("gz"));
    }

    @Test
    public void testSinglePointInFileName() {
        Path path = Paths.get("/test/test.txt");
        assertThat(extension(path), is("txt"));
    }

    @Test
    public void testPointInDirectoryName() {
        Path path = Paths.get("test.d/test");
        assertThat(extension(path), is(""));
    }

    @Test
    public void testNoPointInPathName() {
        Path path = Paths.get("test/test");
        assertThat(extension(path), is(""));
    }

}
