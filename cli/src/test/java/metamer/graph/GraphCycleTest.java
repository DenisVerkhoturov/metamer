package metamer.graph;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.hasValue;

public class GraphCycleTest {

    @Test
    @DisplayName("colouredNodes should contain all nodes when the search of cycle in graph ended")
    public void testIfAllNodesArePresent() {
        Graph graph = new Graph(new HashMap<>(), new HashMap<>(), 3);
        graph.createFromStream(Stream.of("AATR", "ART"));
        GraphCycle graphCycle = new GraphCycle(graph, 3);

        final Map<Node, GraphCycle.NodeDescriptor> colouredNodes = graphCycle.getColouredNodes();
        for (final Map.Entry<String, Node> entry : graph.getNodes().entrySet()) {
            assertThat(colouredNodes, hasKey(entry.getValue()));
        }

        final Map<String, Node> nodes = graph.getNodes();
        for (final Map.Entry<Node, GraphCycle.NodeDescriptor> entry : colouredNodes.entrySet()) {
            assertThat(nodes, hasValue(entry.getKey()));
        }
    }

    @Test
    @DisplayName("colouredNodes should contain only white nodes when nodes are uninitialized")
    public void testAllUninitializedNodesAreWhite() {
        Graph graph = new Graph(new HashMap<>(), new HashMap<>(), 3);
        graph.createFromStream(Stream.of("ABCDE"));
        GraphCycle graphCycle = new GraphCycle(graph, 3);
        assertThat(graphCycle.getColouredNodes(), not(hasValue(GraphCycle.NodeColor.GRAY)));
        assertThat(graphCycle.getColouredNodes(), not(hasValue(GraphCycle.NodeColor.BLACK)));
    }

    @Test
    @DisplayName("colouredNodes should contain only black nodes when nodes are initialized")
    public void testAllInitializedNodesAreBlack() {
        Graph graph = new Graph(new HashMap<>(), new HashMap<>(), 3);
        graph.createFromStream(Stream.of("ABCDEA"));
        GraphCycle graphCycle = new GraphCycle(graph, 3);
        graphCycle.findCycle();
        assertThat(graphCycle.getColouredNodes(), not(hasValue(GraphCycle.NodeColor.WHITE)));
        assertThat(graphCycle.getColouredNodes(), not(hasValue(GraphCycle.NodeColor.GRAY)));
    }

    @Test
    @DisplayName("graphCycle field should be empty when there is no cycle in graph")
    public void testNoCycleInGraph() {
        Graph graph = new Graph(new HashMap<>(), new HashMap<>(), 3);
        graph.createFromStream(Stream.of("ABCDE"));
        GraphCycle graphCycle = new GraphCycle(graph, 3);
        assertThat(graphCycle.findCycle(), is(""));
    }

    @Test
    @DisplayName("graphCycle field should contain correct cycle when there is cycle in graph")
    public void testCorrectCycleGraph() {
        Graph graph = new Graph(new HashMap<>(), new HashMap<>(), 3);
        graph.createFromStream(Stream.of("ABCDEA"));
        graph = graph.optimizeGraph();
        GraphCycle graphCycle = new GraphCycle(graph, 3);
        assertThat(graphCycle.findCycle(), isOneOf("ABCDEA", "BCDEAB", "CDEABC", "DEABCD", "EABCDE"));
    }
}
