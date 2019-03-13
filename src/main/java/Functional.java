import java.util.List;
import java.util.NoSuchElementException;

public class Functional {
    static int sum(List<Integer> list) {
        int sum = 0;
        for (int i : list) {
            sum += i;
        }
        return sum;
    }

    static int max(List<Integer> list)  throws NoSuchElementException {
        if (list.size() != 0) {
            int max = list.get(0);
            for (int i : list) {
                if (i > max) {
                    max = i;
                }
            }
            return max;
        } else  {
            throw new NoSuchElementException();
        }
    }
}


