package metamer.graph;

import metamer.utils.GraphUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Graph {

    private Map<Node, List<Node>> neighbors;
    private Map<String, Node> nodes;
    private int k;

    public Graph(final Map<Node, List<Node>> neighbors, final Map<String, Node> nodes, int k) {
        this.neighbors = neighbors;
        this.nodes = nodes;
        this.k = k;
    }

    private void createNodesMap(String str) {
        this.nodes.computeIfAbsent(str.substring(0, k - 1), Node::new);
        this.nodes.computeIfAbsent(str.substring(1, k), Node::new);
    }

    public void makeNodes(String str) {
        GraphUtils.slidingWindow(str, k).forEach(this::createNodesMap);
    }

    public Stream<Map.Entry<String, Node>> getNodesAsCollection() {
        return this.nodes.entrySet().stream();
    }

    public Map<String, Node> getNodes() {
        return new HashMap<>(this.nodes);
    }

    public Map<Node, List<Node>> getNeighbors() {
        return new HashMap<>(this.neighbors);
    }

    public Graph optimizeGraph() {
        Map<String, Node> nodesOptimized = new HashMap<>();
        Map<Node, List<Node>> neighborsOptimized = neighbors;
        Set<String> used = new HashSet<>();

        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            final Node first = entry.getValue();
            if (used.contains(first.kmer)) {
                continue;
            }
            used.add(first.kmer);
            String newKmer = first.kmer;
            Node last = first;
            while ((last.nout == 1) && (neighborsOptimized.get(last).get(0).nin == 1) &&
                    (neighborsOptimized.get(last).get(0) != first)) {
                final Node tmp = last;
                last = neighborsOptimized.get(last).get(0);
                neighborsOptimized.remove(tmp);
                nodesOptimized.remove(tmp.kmer);
                used.add(last.kmer);
                newKmer += last.kmer.substring(k - 2);
            }

            if (newKmer.equals(first.kmer)) {
                nodesOptimized.put(first.kmer, first);
                continue;
            }

            Node node = new Node(newKmer);
            node.nin = first.nin;
            node.nout = last.nout;
            nodesOptimized.put(newKmer, node);

            neighborsOptimized.put(node, neighborsOptimized.get(last));
            neighborsOptimized.remove(last);
            nodesOptimized.remove(last.kmer);
            for (Map.Entry<Node, List<Node>> entry2 : neighborsOptimized.entrySet()) {
                if (entry2.getValue().contains(first)) {
                    entry2.getValue().add(node);
                    entry2.getValue().remove(first);
                }
            }
        }
        return new Graph(neighborsOptimized, nodesOptimized, k);
    }

    public void createFromStream(Stream<String> stream) {
        stream.forEach(this::makeNodes);
        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            for (Map.Entry<String, Node> entry2 : nodes.entrySet()) {
                List<Node> list = this.neighbors.getOrDefault(entry.getValue(), new ArrayList<>());

                final String firstKey = entry.getKey().substring(1);
                final String secondKey = entry2.getKey().substring(0, entry2.getKey().length() - 1);
                if (firstKey.equals(secondKey) && !list.contains(entry2.getValue())) {
                    list.add(entry2.getValue());
                    entry.getValue().nout++;
                    entry2.getValue().nin++;
                }
                this.neighbors.put(entry.getValue(), list);
            }
        }
    }

    public static Graph of(final int k, final Map.Entry<Node, Node>... edges) {
        final Map<String, Node> nodes = new HashMap<>();
        final Map<Node, List<Node>> neighbors = new HashMap<>();
        for (Map.Entry<Node, Node> edge : edges) {
            final Node left = edge.getKey();
            final Node right = edge.getValue();
            nodes.putIfAbsent(left.kmer, left);
            nodes.putIfAbsent(right.kmer, right);
            nodes.get(left.kmer).nout++;
            nodes.get(right.kmer).nin++;
            neighbors.merge(nodes.get(left.kmer), new ArrayList<>(Arrays.asList(nodes.get(right.kmer))),
                    (o, n) -> Stream.concat(o.stream(), n.stream()).collect(toList()));
            neighbors.merge(nodes.get(right.kmer), new ArrayList<Node>(),
                    (o, n) -> Stream.concat(o.stream(), n.stream()).collect(toList()));
        }
        return new Graph(neighbors, nodes, k);
    }
}

