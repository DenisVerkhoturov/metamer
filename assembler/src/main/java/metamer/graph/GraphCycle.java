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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;

/**
 * Class to find Euler's ways in de Bruijn graph.
 */
public class GraphCycle {
    private final Graph graph;
    private final int k;
    private final List<String> contigs;

    /**
     * Constructor - initializing fields & paint all nodes into WHITE color.
     *
     * @param graph Pptimized version of graph from previous stages.
     * @param k     Length of kmer.
     */
    public GraphCycle(final Graph graph, final int k) {
        this.graph = graph;
        this.k = k;
        this.contigs = new ArrayList<>();
    }

    /**
     * Realization of finding Eiler's path.
     *
     * @param currentNode Node with start position, tree root.
     * @param str         current version of result string.
     * @param map         map with node and all his neighbours.
     */
    private void dfc(final Node currentNode, final String str, final List<Edge> edges) {
        List<Edge> relatedNodes = new ArrayList<>();
        for (final Edge edge : edges) {
            if (edge.current.equals(currentNode)) {
                relatedNodes.add(edge);
            }
        }

        if (relatedNodes.size() != 0) {
            for (final Edge edge : relatedNodes) {
                edges.remove(edge);
                dfc(edge.next, str + currentNode.kmer.substring(0, currentNode.kmer.length() - (k - 2)), edges);
                edges.add(edge);
            }
        } else {
            contigs.add(str + currentNode.kmer);
        }
    }

    /**
     * Function for finding node with minimum nin.
     *
     * @return stream of strings equal with the first string length.
     */
    public Stream<String> findCycle() {
        graph
                .nodes()
                .stream()
                .min(comparingInt(Node::nin))
                .ifPresent(entryPoint -> dfc(entryPoint, "", new ArrayList<>(graph.edges())));
        contigs.sort(comparing(String::length).reversed());
        return contigs.stream().filter(line -> line.length() == contigs.get(0).length());
    }
}
