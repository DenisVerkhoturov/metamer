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
package metamer.graph;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static metamer.graph.Graph.graph;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;


public class GraphCycleTest {
    @Test
    @DisplayName("graphCycle field should contain correct cycle when there is cycle in graph")
    public void testCorrectCycleGraph() {
        Graph graph = graph(3, Stream.of("ABCDE")).optimizeGraph();
        GraphCycle graphCycle = new GraphCycle(graph, 3);
        assertThat(graphCycle.findCycle().collect(toList()), contains("ABCDE"));
    }

    @Test
    @DisplayName("graphCycle field should contain correct cycle when there are several Records with cycle")
    public void testCorrectCycleGraphFromSeveralRecords() {
        Graph graph = graph(3, Stream.of("ABCDE", "CDERTTT", "TTQWERY")).optimizeGraph();
        GraphCycle graphCycle = new GraphCycle(graph, 3);
        assertThat(graphCycle.findCycle().collect(toList()), contains("ABCDERTTTQWERY"));
    }
}
