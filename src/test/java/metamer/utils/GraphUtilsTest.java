package metamer.utils;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

class GraphUtilsTest {
    @Test
    public void testSlidingWindow() {
        final String s = "BELLE";
        List<String> list = GraphUtils.slidingWindow(s, 3)
                .collect(Collectors.toList());
        assertThat(list, contains("BEL", "ELL", "LLE"));
    }

    @Test
    public void testIfStrIsShorterThanKmer() {
        final String s = "RAT";
        List<String> list = GraphUtils.slidingWindow(s, 5)
                .collect(Collectors.toList());
        assertThat(list, empty());
    }
}
