package main;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Main {

    public static int sum(List<Integer> myList) {
        int sum = 0;
        for (int elem : myList) {
            sum += elem;
        }
        return sum;
    }

    public static int max(List<Integer> myList) {
        if (myList.isEmpty()) {
            throw new NoSuchElementException();
        }
        int max = myList.get(0);
        for (int elem : myList) {
            if (elem > max) {
                max = elem;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        List<Integer> myList = new ArrayList<Integer>();
        for (int i = 5; i > -8; i--) {
            myList.add(i);
        }
        System.out.println(sum(myList));
        System.out.println(max(myList));
    }

}
