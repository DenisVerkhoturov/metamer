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
import java.util.List;

import static java.util.stream.Collectors.toList;
import static metamer.functional.tests.Utils.temporaryPath;
import static metamer.utils.Strings.multiline;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SequentialTest {
    private final String pushkin = multiline(
           " Люблю я в полдень воспаленный",
            "Прохладу черпать из ручья",
            "И в роще тихой, отдаленной",
            "Смотреть, как плещет в брег струя.",
            "Когда ж вино в края поскачет,",
            "Напеиясь в чаше круговой,",
            "Друзья, скажите,— кто не плачет,",
            "Заране радуясь душой?",

            "Да будет проклят дерзновенный,",
            "Кто первый грешною рукой,",
            "Нечестьем буйным ослепленный,",
            "О страх!., смесил вино с водой!",
            "Да будет проклят род злодея!",
            "Пускай не в силах будет пить",
            "Или, стаканами владея,",
            "Лафит с цымлянским различить!"
    );

    @Test
    @DisplayName("rhyme should be assemble correctly when ran")
    public void puskinTest() throws IOException {
        final Path inputPath = Files.createTempFile("inp", ".fasta");
        Files.write(inputPath, ">id 0 test\n".getBytes());
        Files.write(inputPath, pushkin.getBytes(), StandardOpenOption.APPEND);

        final Path outputPath = temporaryPath("out", "fasta");

        final String expected = ">seq1 Люблю я в полдень воспаленный"
                + "Прохладу черпать из ручья"
                + "И в роще тихой, отдаленной"
                + "Смотреть, как плещет в брег струя."
                + "Когда ж вино в края поскачет,"
                + "Напеиясь в чаше круговой,"
                + "Друзья, скажите,— кто не плачет,"
                + "Заране радуясь душой?"
                + "Да будет проклят дерзновенный,"
                + "Кто первый грешною рукой,"
                + "Нечестьем буйным ослепленный,"
                + "О страх!., смесил вино с водой!"
                + "Да будет проклят род злодея!"
                + "Пускай не в силах будет пить"
                + "Или, стаканами владея,"
                + "Лафит с цымлянским различить!";

        CliHandler.main("-k", "18", "-format", "fasta", "-i", inputPath.toString(), "-o", outputPath.toString());
        StringBuilder res = new StringBuilder();
        List<String> list = Files.lines(outputPath).collect(toList());
        for (final String s : list) {
            res.append(s);
        }
        assertThat(res.toString(), is(expected));
    }
}
