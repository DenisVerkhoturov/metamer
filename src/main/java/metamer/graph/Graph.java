package metamer.graph;

import metamer.utils.GraphUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Graph {

    private Map<Node, ArrayList<Node>> edges;
    private Map<String, Node> nodes;
    private int k;

    class Node {
        private String kmer;
        private int nin;
        private int nout;

        Node(String kmer) {
            this.kmer = kmer;
            this.nin = 0;
            this.nout = 0;
        }

        public String getKmer() {
            return this.kmer;
        }

        @Override
        public String toString() {
            return " Kmer = " + this.kmer + ", nout = " + this.nout + ", nin = " + this.nin + " ";
        }
    }

    private void createNodesMap(String st) {
        Node nodeL;
        Node nodeR;
        final String km1L = st.substring(0, k - 1);
        final String km1R = st.substring(1, k);

        if (this.nodes.containsKey(km1L)) {
            nodeL = this.nodes.get(km1L);
        } else {
            this.nodes.put(km1L, new Node(km1L));
            nodeL = this.nodes.get(km1L);
        }
        if (this.nodes.containsKey(km1R)) {
            nodeR = this.nodes.get(km1R);
        } else {
            this.nodes.put(km1R, new Node(km1R));
            nodeR = this.nodes.get(km1R);
        }
        nodeL.nout += 1;
        nodeL.kmer = km1L;
        nodeR.nin += 1;
        nodeR.kmer = km1R;
    }

    private void makeNodes(String st) {
       GraphUtils.slidingWindow(st, k).forEach(this::createNodesMap);
    }

    public Stream<Map.Entry<String, Node>> getNodesAsCollection() {
        return this.nodes.entrySet().stream();
    }

    public Map<String, Node> getNodes() {
        return new HashMap<>(this.nodes);
    }

    public Map<Node, ArrayList<Node>> getEdges() {
        return new HashMap<>(this.edges);
    }

    Graph(Stream<String> strStream, int k) {

        this.edges = new HashMap<>();
        this.nodes = new HashMap<>();
        this.k = k;

        strStream.forEach(this::makeNodes);

        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            for (Map.Entry<String, Node> entry2 : nodes.entrySet()) {
                ArrayList<Node> list = this.edges.getOrDefault(entry.getValue(), new ArrayList<>());

                final String firstKey = entry.getKey().substring(1);
                final String secondKey = entry2.getKey().substring(0, entry2.getKey().length() - 1);
                if (firstKey.equals(secondKey) && !list.contains(entry2.getValue())) {
                    list.add(entry2.getValue());
                }
                this.edges.put(entry.getValue(), list);
            }
        }
    }
}

