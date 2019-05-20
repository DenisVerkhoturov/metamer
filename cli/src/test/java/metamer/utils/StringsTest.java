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

import java.util.List;
import java.util.stream.Collectors;

import static metamer.utils.Strings.windows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

class StringsTest {
    @Test
    @DisplayName("list should contain all possible windows when finish sliding")
    public void testSlidingWindow() {
        final String s = "BELLE";
        final List<String> list = windows(s, 3).collect(Collectors.toList());
        assertThat(list, contains("BEL", "ELL", "LLE"));
    }

    @Test
    @DisplayName("list should be empty if window size is bigger than string")
    public void testIfStrIsShorterThanKmer() {
        final String s = "RAT";
        final List<String> list = windows(s, 5).collect(Collectors.toList());
        assertThat(list, empty());
    }
}
