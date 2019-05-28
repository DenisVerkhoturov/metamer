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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.equalTo;


class GraphTest {
    @Test
    @DisplayName("all nodes should present when create graph")
    public void testIfAllNodesArePresent() {
        Graph gr = new Graph(new HashMap<>(), new HashMap<>(), 3);
        gr.createFromStream(Stream.of("AATR", "ART"));
        assertThat(gr.getNodes(), hasKey("AA"));
        assertThat(gr.getNodes(), hasKey("AT"));
        assertThat(gr.getNodes(), hasKey("AR"));
        assertThat(gr.getNodes(), hasKey("TR"));
        assertThat(gr.getNodes(), hasKey("RT"));
    }

    @Test
    @DisplayName("nodes should be connected when there is only one way to connect them")
    public void testOptimization() {
        Graph gr = Graph.of(
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
        GraphCycle t = new GraphCycle(optGr, 3);
        assertThat(optGr.getNodes(), hasKey("AT"));
        assertThat(optGr.getNodes(), hasKey("TG"));
        assertThat(optGr.getNodes(), hasKey("GG"));
        assertThat(optGr.getNodes(), hasKey("GC"));
        assertThat(optGr.getNodes(), hasKey("CGT"));
        assertThat(optGr.getNodes(), hasKey("CA"));
    }

    @Test
    @DisplayName("node should not present if there is no way to connect parts")
    public void testIfNodeNotPresent() {
        Graph gr = new Graph(new HashMap<>(), new HashMap<>(), 3);
        gr.createFromStream(Stream.of("AAA", "TTT"));
        assertThat(gr.getNodes(), not(hasKey("AT")));
    }

    @Test
    @DisplayName("node should have information about all neighbors when graph created")
    public void testIfEdgeContainsAllNodes() {
        Graph gr = new Graph(new HashMap<>(), new HashMap<>(), 3);
        gr.createFromStream(Stream.of("AARAT", "AAT"));
        assertThat(gr.getNeighbors().get(gr.getNodes().get("AA")), hasSize(3));
    }

    @Test
    @DisplayName("kmer field should be initializes correctly when graph created")
    public void testCorrectnessOfNodeKmerField() {
        Graph gr = new Graph(new HashMap<>(), new HashMap<>(), 3);
        gr.createFromStream(Stream.of("AARAT", "AAT"));
        assertThat(gr.getNodes().get("AA").kmer, equalTo("AA"));
    }

}
