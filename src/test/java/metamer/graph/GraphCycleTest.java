package metamer.graph;

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
    public void testIfAllNodesArePresent() {
        Graph graph = new Graph(new HashMap<>(), new HashMap<>(), 3);
        graph.createFromStream(Stream.of("AATR", "ART"));
        GraphCycle graphCycle = new GraphCycle(graph);

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
    public void testAllUninitializedNodesAreWhite() {
        Graph graph = new Graph(new HashMap<>(), new HashMap<>(), 3);
        graph.createFromStream(Stream.of("ABCDE"));
        GraphCycle graphCycle = new GraphCycle(graph);
        assertThat(graphCycle.getColouredNodes(), not(hasValue(GraphCycle.NodeColor.GRAY)));
        assertThat(graphCycle.getColouredNodes(), not(hasValue(GraphCycle.NodeColor.BLACK)));
    }

    @Test
    public void testAllInitializedNodesAreBlack() {
        Graph graph = new Graph(new HashMap<>(), new HashMap<>(), 3);
        graph.createFromStream(Stream.of("ABCDEA"));
        GraphCycle graphCycle = new GraphCycle(graph);
        graphCycle.findCycle();
        assertThat(graphCycle.getColouredNodes(), not(hasValue(GraphCycle.NodeColor.WHITE)));
        assertThat(graphCycle.getColouredNodes(), not(hasValue(GraphCycle.NodeColor.GRAY)));
    }

    @Test
    public void testNoCycleInGraph() {
        Graph graph = new Graph(new HashMap<>(), new HashMap<>(), 3);
        graph.createFromStream(Stream.of("ABCDE"));
        GraphCycle graphCycle = new GraphCycle(graph);
        assertThat(graphCycle.findCycle(), is(""));
    }

    @Test
    public void testCorrectCycleGraph() {
        Graph graph = new Graph(new HashMap<>(), new HashMap<>(), 3);
        graph.createFromStream(Stream.of("ABCDEA"));
        graph = graph.optimizeGraph();
        GraphCycle graphCycle = new GraphCycle(graph);
        assertThat(graphCycle.findCycle(), isOneOf("ABCDE", "BCDEA", "CDEAB", "DEABC", "EABCD"));
    }
}
