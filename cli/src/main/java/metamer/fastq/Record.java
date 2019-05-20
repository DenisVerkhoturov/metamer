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

import metamer.io.HasSequence;

import java.util.Arrays;
import java.util.Objects;

public class Record implements HasSequence {
    public final String id;
    public final String description;
    public final String sequence;
    public final byte[] quality;

    public Record(final String id, final String description, final String sequence, final byte[] quality) {
        this.id = id;
        this.description = description;
        this.sequence = sequence;
        this.quality = quality;
    }

    @Override
    public String sequence() {
        return sequence;
    }

    @Override
    public  boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Record rec = (Record)obj;
        return (this.id.equals(rec.id) & this.description.equals(rec.description) &
                this.sequence.equals(rec.sequence) & Arrays.equals(this.quality, rec.quality));
    }

    @Override
    public String toString() {
        return "ID: " + this.id + "; description: " + this.description + "; sequence: " +
                this.sequence + " quality: " + this.quality;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id + description + sequence + quality);
    }
}

