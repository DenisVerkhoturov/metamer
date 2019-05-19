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

import metamer.cmdparser.CliHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.util.stream.Collectors.toList;
import static metamer.functional.tests.Utils.temporaryPath;
import static metamer.utils.Strings.multiline;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class RepetitionTest {
    private final String rhyme = multiline(
            "START I know an old lady who swallowed a fly",
            "I don't know why she swallowed the fly",
            "Perhaps she'll die",

            "I know an old lady who swallowed a spider",
            "That wriggled and jiggled and tickled inside her",
            "She swallowed the spider to catch the fly",
            "But I don't know why she swallowed the fly",
            "Perhaps she'll die",

            "I know an old lady who swallowed a bird",
            "How absurd to swallow a bird",
            "She swallowed the bird to catch the spider",
            "That wriggled and jiggled and tickled inside her",
            "She swallowed the spider to catch the fly",
            "But I don't know why she swallowed the fly",
            "Perhaps she'll die",

            "I know an old lady who swallowed a cat",
            "Imagine that. She swallowed a cat.",
            "She swallowed the cat to catch the bird",
            "She swallowed the bird to catch the spider",
            "That wriggled and jiggled and tickled inside her",
            "She swallowed the spider to catch the fly",
            "But I don't know why she swallowed that fly",
            "Perhaps she'll die",

            "I know an old lady who swallowed a dog",
            "What a hog to swallow a dog!",
            "She swallowed the dog to catch the cat",
            "She swallowed the cat to catch the bird",
            "She swallowed the bird to catch the spider",
            "That wriggled and jiggled and tickled inside her",
            "She swallowed the spider to catch the fly",
            "But I don't know why she swallowed that fly",
            "Perhaps she'll die",

            "I know an old lady who swallowed a goat",
            "Opened her throat and down went the goat!",
            "She swallowed the goat to catch the dog",
            "She swallowed the dog to catch the cat",
            "She swallowed the cat to catch the bird",
            "She swallowed the bird to catch the spider",
            "That wriggled and jiggled and tickled inside her",
            "She swallowed the spider to catch the fly",
            "But I don't know why she swallowed that fly",
            "Perhaps she'll die",

            "I know an old lady who swallowed a cow",
            "I don't know how she swallowed the cow",
            "She swallowed the cow to catch the goat",
            "She swallowed the goat to catch the dog",
            "She swallowed the dog to catch the cat",
            "She swallowed the cat to catch the bird",
            "She swallowed the bird to catch the spider",
            "That wriggled and jiggled and tickled inside her",
            "She swallowed the spider to catch the fly",
            "But I don't know why she swallowed that fly",
            "Perhaps she'll die",

            "I know an old lady who swallowed a horse",
            "She's alive and well of course!"

    );

    @Test
    @DisplayName("rhyme should be assemble correctly when ran")
    public void correctInputTest() throws IOException {
        final Path inputPath = Files.createTempFile("inp", ".fasta");
        Files.write(inputPath, ">id 0 test\n".getBytes());
        Files.write(inputPath, rhyme.getBytes(), StandardOpenOption.APPEND);
        final Path outputPath = temporaryPath("out", "fasta");

        final String expected2 = "START I know an old lady who swallowed a cowI don't know how she swallowed the s,"
                + " pider to catch the spiderThat wriggled and jiggled and tickled inside herShe swa, llowed that "
                + "flyPerhaps she'll dieI know an old lady who swallowed a goatOpened h, er throat and down went the "
                + "goat!She swallowed a flyI don't know why she swallow, ed a spiderThat wriggled"
                + " and jiggled and tickled inside herShe swallowed";

        CliHandler.main("-k", "15", "-format", "fasta", "-i", inputPath.toString(), "-o", outputPath.toString());
        assertThat(Files.lines(outputPath).collect(toList()).toString(), containsString(expected2));
    }
}
