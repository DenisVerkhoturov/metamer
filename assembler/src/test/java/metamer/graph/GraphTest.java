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

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static metamer.graph.Graph.graph;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;


class GraphTest {
    @Test
    @DisplayName("all nodes should present when create graph")
    public void testIfAllNodesArePresent() {
        Graph gr = graph(3, Stream.of("AATR", "ART"));
        assertThat(gr.nodes(), containsInAnyOrder(
                new Node("AA"), new Node("AT"), new Node("AR"), new Node("TR"), new Node("RT")
        ));
    }

    @Test
    @DisplayName("nodes should be connected when there is only one way to connect them")
    public void testOptimization() {
        Graph gr = graph(
                3,
                Map.entry(new Node("AT"), new Node("TG")),
                Map.entry(new Node("TG"), new Node("GG")),
                Map.entry(new Node("GG"), new Node("GC")),
                Map.entry(new Node("GC"), new Node("CG")),
                Map.entry(new Node("CG"), new Node("GT")),
                Map.entry(new Node("GT"), new Node("TG")),
                Map.entry(new Node("TG"), new Node("GC")),
                Map.entry(new Node("GC"), new Node("CA"))
        );
        Graph optGr = gr.optimizeGraph();
        assertThat(gr.nodes(), containsInAnyOrder(
                new Node("AT"), new Node("TG"), new Node("GC"),
                new Node("GG"), new Node("CG"), new Node("GT"), new Node("CA")
        ));
    }

    @Test
    @DisplayName("node should not present if there is no way to connect parts")
    public void testIfNodeNotPresent() {
        Graph gr = graph(3, Stream.of("AAA", "TTT"));
        assertThat(gr.nodes(), not(contains(new Node("AT"))));
    }

    @Test
    @DisplayName("kmer field should be initializes correctly when graph created")
    public void testCorrectnessOfNodeKmerField() {
        Graph gr = graph(3, Stream.of("AARAT", "AAT"));
        Node tmp = new Node("AA");
        assertThat(gr.nodes().stream().filter(data -> Objects.equals(data, tmp))
                .findFirst().get().kmer, equalTo("AA"));
    }

    @Test
    @DisplayName("graph should contain all edges when method called")
    public void testIfAllEdgesPresent() {
        Graph gr = graph(3, Stream.of("ARAT", "TAB"));
        assertThat(gr.edges(), containsInAnyOrder(
                new Edge(new Node("AR"), new Node("RA"), 3),
                new Edge(new Node("RA"), new Node("AT"), 3),
                new Edge(new Node("RA"), new Node("AR"), 3),
                new Edge(new Node("RA"), new Node("AB"), 3),
                new Edge(new Node("TA"), new Node("AT"), 3),
                new Edge(new Node("TA"), new Node("AR"), 3),
                new Edge(new Node("TA"), new Node("AB"), 3),
                new Edge(new Node("AT"), new Node("TA"), 3)
        ));
    }
}
