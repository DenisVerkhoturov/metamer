package TestsForFunctionPackage;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static Function.Functional.max;
import static Function.Functional.sum;

class FunctionalTest  {
    @Test
    void testIfListIsEmptyForSum() {
        ArrayList<Integer> testList = new ArrayList<>();
        assertEquals(0, sum(testList));
    }

    @Test
    void testIfListHasSameElements() {
        int number = 5;
        assertEquals(number * 3, sum(Arrays.asList(5, 5, 5)));
    }

    @Test
    void commutativeTest() {
        assertEquals(sum(Arrays.asList(1, 2, 3)), sum(Arrays.asList(3, 2, 1)));
    }

    @Test
    void associativeTest() {
        assertEquals(sum(Arrays.asList(1, 2, 3, 4)), sum(Arrays.asList(1, 2)) + sum(Arrays.asList(3, 4)));
    }

    @Test
    void testIfListIsNullForMax() {
        Throwable ex = assertThrows(NoSuchElementException.class, () -> {
            max(Collections.emptyList());
        });
        assertNotEquals("", ex.getMessage());
    }

    @Test
    void testIfListContainOnePositionForMax() {
        final int max = 7;
        assertEquals(max, max(Collections.singletonList(max)));
    }

    @Test
    void testIfListContainsMax() {
        List<Integer> testList = Arrays.asList(3, 9, 7, 5, 1);
        assertTrue(testList.contains(max(testList)));
    }

    @Test
    void testIfNextMaxIsLessOrEqualsToPrevious() {
        ArrayList<Integer> testList = new ArrayList<>(Arrays.asList(7, 9, 1, 3, 1));
        while (testList.size() != 1) {
            int max_before = max(testList);
            testList.remove((Integer)max_before);
            int max_after = max(testList);
            assertTrue(max_after <= max_before);
        }
    }
}
