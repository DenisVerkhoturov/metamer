package task;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static task.Functional.max;
import static task.Functional.sum;

public class FunctionalTest {
    @Test
    void zeroSum() {
        assertEquals(0, sum(Collections.emptyList()));
    }

    @Test
    void sumOfEqualElements() {
        assertEquals(5 * 3, sum(asList(5, 5, 5)));
    }

    @Test
    void commutativity() {
        int sum1 = sum(asList(1, 2, 3));
        int sum2 = sum(asList(3, 2, 1));
        assertEquals(sum1, sum2);
    }

    @Test
    void associativity() {
        int sum1 = sum(asList(1, 2, 3, 4));
        int sum2 = sum(asList(1, 2)) + sum(asList(3, 4));
        assertEquals(sum1, sum2);
    }

    @Test
    void maxInEmptyList() {
        assertThrows(NoSuchElementException.class, () -> max(Collections.emptyList()));
    }

    @Test
    void maxInSingletonList() {
        assertEquals(5, max(Collections.singletonList(5)));
    }

    @Test
    void containMax() {
        List<Integer> list = asList(1, 2, 3, 4);
        assertTrue(list.contains(max(list)));
    }

    @Test
    void decreaseList() {
        ArrayList<Integer> list = new ArrayList<>(asList(9, 9, 1, 3, 1));
        Integer max;
        while (list.size() != 1) {
            max = max(list);
            list.remove(max);
            assertTrue(max >= max(list));
        }
    }
}
