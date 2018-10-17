package Tabular;

import java.util.LinkedList;

import static java.lang.Math.pow;

public class Table {

    public Group[] group;


    public Table(int size) {
        size++;
        group = new Group[size];
        for (int i = 0; i < size; i++) {
            group[i] = new Group();
        }
    }

    public int getOnesNum(int implicant) {
        int counter = 0;
        while (implicant != 0) {
            if (implicant % 2 == 1) {
                counter++;
            }
            implicant /= 2;
        }
        return counter;
    }

    public void setGroup(int implicant, boolean d) {
        group[getOnesNum(implicant)].add(implicant, -1, d);//check if step -1 or 0
    }

    public boolean compareDummyDigits(Implicant first, Implicant second) {
        return first.dummyDigits.containsAll(second.dummyDigits);
    }

    public boolean validHamming(int second, int first, int size) {
        int subtract = second - first;
        for (int i = 0; i < size; i++) {
            if (subtract == (int) pow(2, i)) {
                return true;
            }
        }
        return false;
    }

    public void addPrime(Group source, int step, LinkedList<LinkedList<Integer>> primeImplicants,
                         LinkedList<String> expression, int size) {
        if (source.notEmpty()) {
            LinkedList<Integer> combinations = new LinkedList<>();
            Implicant i = source.head;
            while (i != null && i.step == step) {
                if (!i.visited && !i.dontCare) {
                    combinations.clear();
                    combinations.add(i.value);
                    source.combination(i.dummyDigits, combinations, 0, i.dummyDigits.size());

                    LinkedList<Integer> comb = new LinkedList<>();
                    for (Integer x : combinations) {
                        comb.add(x);
                    }

                    if (!contains(primeImplicants, comb)) {
                        primeImplicants.add(comb);
                        expression.add(source.implicantToLiteral(i.value, size - 1, i.dummyDigits));
                    }
                }
                i = i.next;
            }
        }
    }


    public boolean contains(LinkedList<LinkedList<Integer>> primeImplicants, LinkedList<Integer> comb) {
        for (LinkedList<Integer> p : primeImplicants) {
            if (p.containsAll(comb)) {
                return true;
            }
        }
        return false;
    }

    public void copyDummy(LinkedList<Integer> source, LinkedList<Integer> destination) {
        for (Integer x : source) {
            destination.add(x);
        }
    }

    public void compareGroups(Group first, Group second, int step, int size) {
        Implicant f = first.head;
        Implicant s = second.head;

        while (f != null && f.step == step) {
            s = second.head;
            while (s != null && s.step == step) {
                if (compareDummyDigits(f, s)) {
                    if (validHamming(s.value, f.value, size)) {
                        s.visited = true;
                        f.visited = true;
                        Implicant newImplicant = first.add(f.value, step, false);
                        copyDummy(f.dummyDigits, newImplicant.dummyDigits);
                        newImplicant.dummyDigits.add(s.value - f.value);
                    }
                }
                s = s.next;
            }
            f = f.next;
        }
    }

    public void removeDoncaresFromPrime(LinkedList<Integer> doncaresList, LinkedList<LinkedList<Integer>> prime) {
        for (Integer d : doncaresList) {
            for (LinkedList<Integer> p : prime) {
                if (p.contains(d)) {
                    p.remove(d);
                }
            }
        }
    }
}
