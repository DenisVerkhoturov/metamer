package metamer.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static metamer.utils.Paths.extension;

public class PathsTest {
    @Test
    @DisplayName("path should have last extension when there is more than one point in string")
    public void testDoublePointInFileName() {
        Path path = Paths.get("/test/test.tar.gz");
        assertThat(extension(path), is("gz"));
    }

    @Test
    @DisplayName("path should have file's extension when there is only one point in string")
    public void testSinglePointInFileName() {
        Path path = Paths.get("/test/test.txt");
        assertThat(extension(path), is("txt"));
    }

    @Test
    @DisplayName("path should not have extension when there is no extension but a point in string")
    public void testPointInDirectoryName() {
        Path path = Paths.get("test.d/test");
        assertThat(extension(path), is(""));
    }

    @Test
    @DisplayName("path should not have extension when there is no extension in string")
    public void testNoPointInPathName() {
        Path path = Paths.get("test/test");
        assertThat(extension(path), is(""));
    }

}
