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
package metamer.assembler;

import lombok.Value;
import lombok.experimental.Accessors;
import metamer.fasta.Record;
import metamer.graph.Graph;
import metamer.graph.GraphCycle;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static metamer.graph.Graph.graph;

/**
 * Management tool for whole graph assembly cycle.
 */
@Value
@Accessors(fluent = true)
public class Assembler {
    private final Stream<String> reads;
    private final Consumer<Stream<Record>> writer;
    private final int k;

    /**
     * Function for workflow control.
     */
    public void assemble() {
        final Graph graph = graph(k, reads);
        final GraphCycle graphCycle = new GraphCycle(graph.optimizeGraph(), k);
        final AtomicInteger counter = new AtomicInteger();
        final Stream<Record> contigs = graphCycle.findCycle().map(e -> {
            counter.getAndIncrement();
            return new Record("seq" + counter, "", e);
        });
        writer.accept(contigs);
    }
}
