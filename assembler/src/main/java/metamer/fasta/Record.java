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

import metamer.io.HasSequence;

import java.util.Objects;

/**
 * Class for forming fasta records.
 */
public class Record implements HasSequence {
    public final String uniqueIdentifier;
    public final String additionalInformation;
    public final String sequence;

    /**
     * Constructor - initializing fields.
     *
     * @param uniqueIdentifier      Unique identifier for current sequence.
     * @param additionalInformation Additional information for current sequence.
     * @param sequence              Current sequence.
     */
    public Record(final String uniqueIdentifier, final String additionalInformation, final String sequence) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.additionalInformation = additionalInformation;
        this.sequence = sequence;
    }

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Record record = (Record) o;
        return Objects.equals(uniqueIdentifier, record.uniqueIdentifier) &&
                Objects.equals(additionalInformation, record.additionalInformation) &&
                Objects.equals(sequence, record.sequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueIdentifier, additionalInformation, sequence);
    }

    @Override
    public String toString() {
        return "Record{" +
                "uniqueIdentifier='" + uniqueIdentifier + '\'' +
                ", additionalInformation='" + additionalInformation + '\'' +
                ", sequence='" + sequence + '\'' +
                '}';
    }
}
