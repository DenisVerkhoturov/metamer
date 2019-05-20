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

package metamer.functional.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.security.SecureRandom;

public class Utils {
    public static Path temporaryPath(final String prefix, final String suffix) {
        return temporaryPath(Path.of(System.getProperty("java.io.tmpdir")), prefix, suffix);
    }

    public static Path temporaryPath(final Path parent, final String prefix, final String suffix) {
        final String random = Long.toUnsignedString(new SecureRandom().nextLong());
        final String name = prefix + random + suffix;
        final Path outputPath = parent.resolve(name);
        outputPath.toFile().deleteOnExit();
        return outputPath;
    }

    public static Path temporaryFile(final String name, final String suffix, final FileAttribute... attributes)
            throws IOException {
        final Path outputPath = Files.createTempFile(name, suffix, attributes);
        outputPath.toFile().deleteOnExit();
        return outputPath;
    }

    public static Path temporaryDirectory(final String name, final FileAttribute... attributes) throws IOException {
        final Path outputPath = Files.createTempDirectory(name, attributes);
        outputPath.toFile().deleteOnExit();
        return outputPath;
    }
}
