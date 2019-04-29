package metamer.functional.tests;

import metamer.cmdparser.CliHandler;
import org.junit.jupiter.api.Disabled;
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
import static org.hamcrest.Matchers.contains;

public class RhymeTest {
    private final String rhyme = multiline(
            "I know an old lady who swallowed a fly",
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
            "She's alive and well of course!I Know a"
    );

    @Disabled("Now finds the shortest cycle")
    @Test
    @DisplayName("rhyme should be assemble correctly when ran")
    public void correctInputTest() throws IOException {
        final Path inputPath = Files.createTempFile("inp", ".fasta");
        Files.write(inputPath, ">id 0 test\n".getBytes());
        Files.write(inputPath, rhyme.getBytes(), StandardOpenOption.APPEND);
        final Path outputPath = temporaryPath("out", "fasta");

        final String expected1 = ">cycle from file: " + inputPath.toString();
        final String expected2 = rhyme;

        CliHandler.main("-k", "5", "-format", "fasta", "-i", inputPath.toString(), "-o", outputPath.toString());
        assertThat(Files.lines(outputPath).collect(toList()), contains(expected1, expected2));
    }
}
