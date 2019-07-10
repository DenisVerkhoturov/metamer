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

import lombok.Value;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static metamer.utils.Strings.windows;

/**
 * Class for building & optimizing de Bruijn graph.
 */
@Value
@Accessors(fluent = true)
public class Graph {

    private final Map<Node, List<Node>> neighbors;
    private final  Map<String, Node> nodes;
    private final int k;

    private void createNodesMap(final String str) {
        this.nodes.computeIfAbsent(str.substring(0, k - 1), Node::new);
        this.nodes.computeIfAbsent(str.substring(1, k), Node::new);
    }

    public void makeNodes(final String str) {
        windows(str, k).forEach(this::createNodesMap);
    }

    /**
     * Get set with nodes of our graph.
     *
     * @return set of nodes.
     */
    public Set<Node> nodes() {
        return new HashSet<>(nodes.values());
    }

    /**
     * Get map of neighbours for every node in graph.
     *
     * @return Hashmap of neighbours.
     */
    public Map<Node, List<Node>> getNeighbors() {
        return new HashMap<>(this.neighbors);
    }

    /**
     * Get edges for current version of graph.
     *
     * @return HashSet of edges.
     */
    public Set<Edge> edges() {
        return neighbors.entrySet().stream()
                .flatMap(e -> e.getValue().stream().map(n -> new Edge(e.getKey(), n, k)))
                .collect(toSet());
    }

    /**
     * Function for optimizing our graph.
     * <p>
     * Here we make one edge from several edges which can be connected only in one way.
     *
     * @return Optimized version of our graph.
     */
    public Graph optimizeGraph() {
        Map<String, Node> nodesOptimized = new HashMap<>();
        Map<Node, List<Node>> neighborsOptimized = neighbors;
        Set<String> used = new HashSet<>();

        for (final Map.Entry<String, Node> entry : nodes.entrySet()) {
            final Node first = entry.getValue();
            if (used.contains(first.kmer)) {
                continue;
            }
            used.add(first.kmer);
            StringBuilder newKmer = new StringBuilder(first.kmer);
            Node last = first;
            while ((last.nout == 1) && (neighborsOptimized.get(last).get(0).nin == 1) &&
                    (neighborsOptimized.get(last).get(0) != first)) {
                final Node tmp = last;
                last = neighborsOptimized.get(last).get(0);
                neighborsOptimized.remove(tmp);
                nodesOptimized.remove(tmp.kmer);
                used.add(last.kmer);
                newKmer.append(last.kmer.substring(k - 2));
            }

            if (newKmer.toString().equals(first.kmer)) {
                nodesOptimized.put(first.kmer, first);
                continue;
            }

            Node node = new Node(newKmer.toString());
            node.nin = first.nin;
            node.nout = last.nout;
            nodesOptimized.put(newKmer.toString(), node);

            neighborsOptimized.put(node, neighborsOptimized.get(last));
            neighborsOptimized.remove(last);
            nodesOptimized.remove(last.kmer);
            for (final Map.Entry<Node, List<Node>> entry2 : neighborsOptimized.entrySet()) {
                if (entry2.getValue().contains(first)) {
                    entry2.getValue().add(node);
                    entry2.getValue().remove(first);
                }
            }
        }

        return new Graph(neighborsOptimized, nodesOptimized, k);
    }

    /**
     * Function for creating initial version of de Bruijn graph from stream of strings.
     *
     * @param stream Stream of strings read from input source.
     */
    public static Graph graph(final int k, final Stream<String> stream) {
        final Map<String, Node> nodes = new HashMap<>();
        final Map<Node, List<Node>> neighbors = new HashMap<>();
        final Graph graph = new Graph(neighbors, nodes, k);
        stream.forEach(graph::makeNodes);
        for (final Map.Entry<String, Node> entry : nodes.entrySet()) {
            for (final Map.Entry<String, Node> entry2 : nodes.entrySet()) {
                List<Node> list = neighbors.getOrDefault(entry.getValue(), new ArrayList<>());

                final String firstKey = entry.getKey().substring(1);
                final String secondKey = entry2.getKey().substring(0, entry2.getKey().length() - 1);
                if (firstKey.equals(secondKey) && !list.contains(entry2.getValue())) {
                    list.add(entry2.getValue());
                    entry.getValue().nout++;
                    entry2.getValue().nin++;
                }
                neighbors.put(entry.getValue(), list);
            }
        }
        return new Graph(neighbors, nodes, k);
    }

    /**
     * Create graph with current edges.
     *
     * @param k     Length of kmer.
     * @param edges Map of edges from input source.
     * @return Initial version of graph.
     */
    @SafeVarargs
    public static Graph graph(final int k, final Map.Entry<Node, Node>... edges) {
        final Map<String, Node> nodes = new HashMap<>();
        final Map<Node, List<Node>> neighbors = new HashMap<>();
        for (final Map.Entry<Node, Node> edge : edges) {
            final Node left = edge.getKey();
            final Node right = edge.getValue();
            nodes.putIfAbsent(left.kmer, left);
            nodes.putIfAbsent(right.kmer, right);
            nodes.get(left.kmer).nout++;
            nodes.get(right.kmer).nin++;
            neighbors.merge(nodes.get(left.kmer), new ArrayList<>(Arrays.asList(nodes.get(right.kmer))),
                    (o, n) -> Stream.concat(o.stream(), n.stream()).collect(toList()));
            neighbors.merge(nodes.get(right.kmer), new ArrayList<>(),
                    (o, n) -> Stream.concat(o.stream(), n.stream()).collect(toList()));
        }
        return new Graph(neighbors, nodes, k);
    }
}

