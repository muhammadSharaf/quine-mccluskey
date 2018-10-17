package Tabular;


import java.util.LinkedList;

public class Group {

    public Implicant head = null;
    public Implicant tail = null;
    public int size = 0;

    public Implicant add(int element, int step, boolean doncare) {
        Implicant newImplicant = new Implicant(element, step, doncare);
        if (head == null) {
            head = newImplicant;
            tail = newImplicant;
        } else {
            tail.next = newImplicant;
            tail = tail.next;
        }
        size++;
        return newImplicant;
    }

    public boolean notEmpty() {
        if (head == null) return false;
        return true;
    }


    public void removeOldImplicants(int step) {
        if (notEmpty()) {
            while (head != null && head.step == step) {
                head = head.next;
            }
        }
    }

    public void combination(LinkedList<Integer> numbers, LinkedList<Integer> sums, int start, int size) {
        if (size == start) {
            return;
        } else {
            combination(numbers, sums, start + 1, size);
        }
        int end = sums.size();
        for (int i = 0; i < end; i++) {
            sums.add(numbers.get(start) + sums.get(i));
        }
    }

    //converting implicant to literal
    public char[] toBinary(int implicantValue, int variableNum) {
        char[] binary = new char[variableNum];
        int i = variableNum - 1;
        while (implicantValue != 0) {
            binary[i] = Character.forDigit(implicantValue % 2, 10);
            i--;
            implicantValue /= 2;
        }
        for (; i >= 0; i--) {
            binary[i] = '0';
        }
        return binary;
    }

    public char[] removeDummy(char[] binary, LinkedList<Integer> dummyDigits, int variableNum) {
        int index;
        for (int digit : dummyDigits) {
            index = (int) (Math.log(digit) / Math.log(2));
            binary[variableNum - index - 1] = '-';
        }
        return binary;
    }

    public String toLiterals(char[] realBinary, int variableNum) {
        StringBuilder literals = new StringBuilder(variableNum * 3);//worst case (literal+compliment+spaces)
        int literalAscii = (int) 'A';
        for (char digit : realBinary) {
            if (digit != '-') {
                literals.append((char) literalAscii);
                if (digit == '0') {
                    literals.append('\'');
                }
                if (literalAscii <= 'A' + variableNum) {
                    literals.append(' ');
                }
            }
            literalAscii++;
        }
        return literals.toString();
    }

    public String implicantToLiteral(int implicantValue, int variableNum, LinkedList<Integer> dummyDigits) {
        char[] binary = toBinary(implicantValue, variableNum);
        char[] realBinary = removeDummy(binary, dummyDigits, variableNum);
        return toLiterals(realBinary, variableNum);
    }
    //end of converting implicant to literal

    public void printGroup() {
        Implicant i = head;
        if (size != 0) {
            while (i != null) {
                System.out.print(i.value + " \t " + i.step + " \t " + i.visited + " \t " + i.dontCare);
                for (Integer x : i.dummyDigits) {
                    System.out.printf(" " + x);
                }
                System.out.println("");
                i = i.next;
            }
        }
    }
}
