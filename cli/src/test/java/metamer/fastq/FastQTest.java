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

package metamer.fastq;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;

public class FastQTest {
    @Test
    @DisplayName("records should contain correct information when file was read")
    void testReadRecordsFromFile() {
        final Either<Exception, Seq<Record>> records = FastQ.parser().read(Stream.of(
                "@MyCoolID some description",
                "ACTGGTCA",
                "+",
                "!~+9#hkm",
                "@MyVeryBeautifulID",
                "NATC",
                "+MyVeryBeautifulID",
                "!!!!"
        ));

        assertThat(records, instanceOf(Either.Right.class));
        assertThat(records.get(), contains(
                new Record("MyCoolID", "some description", "ACTGGTCA", "!~+9#hkm".getBytes()),
                new Record("MyVeryBeautifulID", "", "NATC", "!!!!".getBytes())
        ));
    }
}
