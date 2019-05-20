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
