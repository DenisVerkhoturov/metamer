import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertThrows;

class FunctionalTest extends groovy.util.GroovyTestCase {
    @Test
    void testIfListIsEmptyForSum() {
        ArrayList<Integer> testList = new ArrayList<>();
        assertEquals(0, Functional.sum(testList));
    }

    @Test
    void testIfArrayListHasSameElements() {
        final int number = 5;
        ArrayList<Integer> testList = new ArrayList<>(Arrays.asList(number, number, number));
        assertEquals(number * testList.size(), Functional.sum(testList));
    }

    @Test
    void testIfListHasSameElements() {
        int number = 5;
        assertEquals(number * 3, Functional.sum(Arrays.asList(5, 5, 5)));
    }

    @Test
    void commutativeTest() {
        assertEquals(Functional.sum(Arrays.asList(1, 2, 3)), Functional.sum(Arrays.asList(3, 2, 1)));
    }

    @Test
    void associativeTest() {
        assertEquals(Functional.sum(Arrays.asList(1, 2, 3, 4)), Functional.sum(Arrays.asList(1, 2)) + Functional.sum(Arrays.asList(3, 4)));
    }

    @Test
    void testIfListIsNullForMax() {
        Throwable ex = assertThrows(NoSuchElementException.class, {
            Functional.max(Arrays.asList());
        })
    }

    @Test
    void testIfListContainOnePositionForMax() {
        final int max = 7;
        assertEquals(max, Functional.max(Collections.singletonList(max)));
    }

    @Test
    void testIfListContainsMax() {
        ArrayList<Integer> testList = new ArrayList<>(Arrays.asList(3, 9, 7, 5, 1));
        assertTrue(testList.contains(Functional.max(testList)));
    }

    @Test
    void testIfNextMaxIsLessOrEqualsToPrevious() {
        ArrayList<Integer> testList = new ArrayList<>(Arrays.asList(7, 9, 1, 3, 1));
        while (testList.size() != 1) {
            /*type conversion suddenly stopped working here :( */
            int max_index = testList.indexOf(Functional.max(testList))
            int max_before = Functional.max(testList);
            testList.remove(max_index);
            int max_after = Functional.max(testList);
            assertTrue(max_after <= max_before);
        }
    }
}
