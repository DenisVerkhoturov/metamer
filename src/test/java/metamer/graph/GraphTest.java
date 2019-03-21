package metamer.graph;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.equalTo;


class GraphTest {
    @Test
    public void testIfAllNodesArePresent() {
        Graph gr = new Graph(Stream.of("AATR", "ART"), 3);
        assertThat(gr.getNodes(), hasKey("AA"));
        assertThat(gr.getNodes(), hasKey("AT"));
        assertThat(gr.getNodes(), hasKey("AR"));
        assertThat(gr.getNodes(), hasKey("TR"));
        assertThat(gr.getNodes(), hasKey("RT"));

    }

    @Test
    public void testIfNodeNotPresent() {
        Graph gr = new Graph(Stream.of("AAA", "TTT"), 3);
        assertThat(gr.getNodes(), not(hasKey("AT")));
    }

    @Test
    public void testIfEdgeContainsAllNodes() {
        Graph gr = new Graph(Stream.of("AARAT", "AAT"), 3);
        assertThat(gr.getEdges().get(gr.getNodes().get("AA")), hasSize(3));
    }

    @Test
    public void testCorrectnessOfNodeKmerField() {
        Graph gr = new Graph(Stream.of("AARAT", "AAT"), 3);
        assertThat(gr.getNodes().get("AA").getKmer(), equalTo("AA"));
    }

}
