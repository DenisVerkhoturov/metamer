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
package metamer.fasta;


import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import metamer.io.HasSequence;


/**
 * Class for forming fasta records.
 */
@Value
@RequiredArgsConstructor
@Accessors(fluent = true)
public class Record implements HasSequence {
    public final String uniqueIdentifier;
    public final String additionalInformation;
    public final String sequence;

    /**
     * Constructor - initializing fields & there is no additional information.
     * @param uniqueIdentifier Unique identifier for current sequence.
     * @param sequence         Current sequence.
     */
    public Record(final String uniqueIdentifier, final String sequence) {
        this(uniqueIdentifier, "", sequence);
    }

    @Override
    public String sequence() {
        return sequence;
    }
}
