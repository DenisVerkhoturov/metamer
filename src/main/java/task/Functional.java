package task;

import java.util.List;
import java.util.NoSuchElementException;

public class Functional {
    public static int sum(List<Integer> list) {
        int sum = 0;
        for (Integer elem : list) {
            sum += elem;
        }
        return sum;
    }

    public static int max(List<Integer> list) throws NoSuchElementException {
        if (list.isEmpty())
            throw new NoSuchElementException("Cannot define max element because the list is empty!");
        int max = list.get(0);
        for (Integer elem : list) {
            if (elem > max) {
                max = elem;
            }
        }
        return max;
    }
}
