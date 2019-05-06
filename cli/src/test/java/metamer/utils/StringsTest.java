package metamer.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static metamer.utils.Strings.windows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

class StringsTest {
    @Test
    @DisplayName("list should contain all possible windows when finish sliding")
    public void testSlidingWindow() {
        final String s = "BELLE";
        final List<String> list = windows(s, 3).collect(Collectors.toList());
        assertThat(list, contains("BEL", "ELL", "LLE"));
    }

    @Test
    @DisplayName("list should be empty if window size is bigger than string")
    public void testIfStrIsShorterThanKmer() {
        final String s = "RAT";
        final List<String> list = windows(s, 5).collect(Collectors.toList());
        assertThat(list, empty());
    }
}
