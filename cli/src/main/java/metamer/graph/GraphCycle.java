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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GraphCycle {
    private final Graph graph;
    private Map<Node, NodeDescriptor> colouredNodes;
    private Node startCycle, endCycle;
    private int k;

    enum NodeColor {
        WHITE, GRAY, BLACK
    }

    class NodeDescriptor {
        private NodeColor color;
        private Node previousNode;

        NodeDescriptor() {
            this.color = NodeColor.WHITE;
            this.previousNode = null;
        }
    }

    public GraphCycle(final Graph graph, final int k) {
        this.graph = graph;
        this.k = k;

        colouredNodes = new HashMap<>();
        for (final Map.Entry<String, Node> entry : graph.getNodes().entrySet()) {
            colouredNodes.put(entry.getValue(), new NodeDescriptor());
        }
    }

    public Map<Node, NodeDescriptor> getColouredNodes() {
        return new HashMap<>(this.colouredNodes);
    }

    private void dfc(final Node currentNode, final NodeDescriptor currentDescriptor) {
        currentDescriptor.color = NodeColor.GRAY;
        final List<Node> relatedNodes = graph.getNeighbors().get(currentNode);
        for (final Node relatedNode : relatedNodes) {
            final NodeDescriptor relatedNodeDescriptor = colouredNodes.get(relatedNode);
            if (relatedNodeDescriptor.color.equals(NodeColor.WHITE)) {
                relatedNodeDescriptor.previousNode = currentNode;
                dfc(relatedNode, relatedNodeDescriptor);
            }
            if (relatedNodeDescriptor.color.equals(NodeColor.GRAY)) {
                startCycle = currentNode;
                endCycle = relatedNode;
                break;
            }
        }
        currentDescriptor.color = NodeColor.BLACK;
    }

    private String createCycle() {
        if (startCycle == null || endCycle == null) {
            return "";
        }

        if ((Objects.equals(startCycle, endCycle)) &&
                (startCycle.kmer.charAt(0) == endCycle.kmer.charAt(endCycle.kmer.length() - 1))) {
            return startCycle.kmer.substring(0, startCycle.kmer.length() - 1);
        }
        final StringBuilder tmpStr = new StringBuilder();
        tmpStr.insert(0, startCycle.kmer.substring(0, startCycle.kmer.length() - 2));
        while (!Objects.equals(startCycle, endCycle)) {
            startCycle = colouredNodes.get(startCycle).previousNode;
            tmpStr.insert(0, startCycle.kmer.substring(0, startCycle.kmer.length() - (k / 2 + 1)));
        }
        return tmpStr.toString();
    }

    public String findCycle() {
        if (graph.getNodes().size() == 1) return graph.getNodes().values().toArray()[0].toString();
        for (final Map.Entry<String, Node> entry : graph.getNodes().entrySet()) {
            final Node currentNode = entry.getValue();
            final NodeDescriptor currentDescriptor = colouredNodes.get(currentNode);
            if (currentDescriptor.color.equals(NodeColor.WHITE)) {
                dfc(currentNode, currentDescriptor);
            }
        }
        return createCycle();
    }
}
