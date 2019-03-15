package task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class Main {

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 4, 5, 9));
        try {
            System.out.println("Sum is " + Functional.sum(list));
            System.out.println("Max is " + Functional.max(list));
        } catch (NoSuchElementException error) {
            System.out.println("List is empty!");
        }
    }
}
