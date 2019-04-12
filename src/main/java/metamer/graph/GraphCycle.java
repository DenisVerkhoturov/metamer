package metamer.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GraphCycle {
    private final Graph graph;
    private Map<Graph.Node, NodeDescriptor> colouredNodes;
    private Graph.Node startCycle, endCycle;

    enum NodeColor {
        WHITE, GRAY, BLACK
    }

    class NodeDescriptor {
        private NodeColor color;
        private Graph.Node previousNode;

        NodeDescriptor() {
            this.color = NodeColor.WHITE;
            this.previousNode = null;
        }
    }

    public GraphCycle(final Graph graph) {
        this.graph = graph;

        colouredNodes = new HashMap<>();
        for (Map.Entry<String, Graph.Node> entry : graph.getNodes().entrySet()) {
            colouredNodes.put(entry.getValue(), new NodeDescriptor());
        }
    }

    public Map<Graph.Node, NodeDescriptor> getColouredNodes() {
        return new HashMap<>(this.colouredNodes);
    }

    private void dfc(final Graph.Node currentNode, final NodeDescriptor currentDescriptor) {
        currentDescriptor.color = NodeColor.GRAY;
        final ArrayList<Graph.Node> relatedNodes = graph.getEdges().get(currentNode);
        for (Graph.Node relatedNode : relatedNodes) {
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
            System.out.println("No cycle in this graph!");
            return "";
        }

        final StringBuilder tmpStr = new StringBuilder();
        tmpStr.insert(0, startCycle.getKmer().substring(0, 1));
        while (!Objects.equals(startCycle, endCycle)) {
            startCycle = colouredNodes.get(startCycle).previousNode;
            tmpStr.insert(0, startCycle.getKmer().substring(0, 1));
        }
        return tmpStr.toString();
    }

    public String findCycle() {
        for (Map.Entry<String, Graph.Node> entry : graph.getNodes().entrySet()) {
            final Graph.Node currentNode = entry.getValue();
            final NodeDescriptor currentDescriptor = colouredNodes.get(currentNode);
            if (currentDescriptor.color.equals(NodeColor.WHITE)) {
                dfc(currentNode, currentDescriptor);
            }
        }
        return createCycle();
    }
}
