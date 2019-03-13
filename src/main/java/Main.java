import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Main {

    public static void main(String[] args) {
        try {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(7);
            list.add(3);
            list.add(9);
            list.add(21);
            list.add(18);
            System.out.println("Sum of list " + Functional.sum(list));
            System.out.println("Max in list " + Functional.max(list));
        } catch (NoSuchElementException n_s_e) {
            n_s_e.printStackTrace();
            System.out.println("List is empty!");
        }
    }
}
