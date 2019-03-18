package testsformain;

import main.Main;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

class MainTests {
    @Test
    void testForEmptyList() {
        assertEquals(0, Main.sum(new ArrayList<Integer>()));
    }

    @Test
    void testForListOfEqualElements() {
        assertEquals(5 * 3, Main.sum(Arrays.asList(5, 5, 5)));
    }

    @Test
    void testCommutative() {
        assertEquals(Main.sum(Arrays.asList(1, 2, 3)), Main.sum(Arrays.asList(3, 2, 1)));
    }

    @Test
    void testAssociative() {
        assertEquals(Main.sum(Arrays.asList(1, 2, 3, 4)), Main.sum(Arrays.asList(1, 2)) + Main.sum(Arrays.asList(3, 4)));
    }

    @Test
    void testForMaxOfEmptyList() {
        assertThrows(NoSuchElementException.class, () -> Main.max(new ArrayList<Integer>()));
    }

    @Test
    void testForListWithOneElement() {
        assertEquals(5, Main.sum(Arrays.asList(5)));
    }

    @Test
    void testIfListContainsMax() {
        List<Integer> list = Arrays.asList(5, 2, 4, 1, 3);
        assertTrue(list.contains(Main.max(list)));
    }

    @Test
    void testForNextMaxElement() {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(5, 2, 4, 1, 3));
        while (list.size() > 1) {
            int prevMax = Main.max(list);
            list.remove((Integer)prevMax);
            assertTrue(Main.max(list) <= prevMax);
        }
    }
}

