package Tabular;

import java.util.LinkedList;

public class Implicant {

    public int value;
    public boolean visited = false;
    public boolean dontCare = false;
    public Implicant next = null;
    public int step = 0;
    public LinkedList<Integer> dummyDigits = new LinkedList<>();


    public Implicant(int element, int k, boolean d) {
        value = element;
        step = k + 1;
        dontCare = d;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isDontCare() {
        return dontCare;
    }

}
