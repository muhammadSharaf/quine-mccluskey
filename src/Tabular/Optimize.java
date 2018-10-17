package Tabular;

import java.util.LinkedList;

public class Optimize {

    public void mintermArray(LinkedList<String>[] arr, LinkedList<LinkedList<Integer>> prime,
                             LinkedList<String> expression) {
        for (int i = 0; i < prime.size(); i++) {
            for (Integer x : prime.get(i)) {
                arr[x].add(expression.get(i));
            }
        }
    }

    // find essential implicants
    private void findEsential(LinkedList<LinkedList<Integer>> prime, LinkedList<LinkedList<Integer>> essentials,
                              LinkedList<String> essentialExpresion, LinkedList<String> expression,
                              LinkedList<Integer> rowRemoved) {
        int counter = 0;
        for (int i = 0; i < prime.size(); i++) {
            for (Integer m : prime.get(i)) {
                counter = 0;
                for (int k = 0; k < prime.size(); k++) {
                    if (prime.get(k).contains(m)) {
                        counter++;
                    }
                }
                if (counter == 1) {
                    LinkedList<Integer> p = new LinkedList<>();
                    p.addAll(prime.get(i));
                    if (!essentials.contains(p)) {
                        rowRemoved.add(m);
                        essentials.add(p);
                        essentialExpresion.add(expression.get(i));
                    }
                }
            }
        }
    }

    private static void deleteCoveredMinterms(LinkedList<LinkedList<Integer>> prime,
                                              LinkedList<LinkedList<Integer>> essentials) {

        for (LinkedList<Integer> essential : essentials) {
            for (Integer m : essential) {
                for (LinkedList<Integer> p : prime) {
                    if (p.contains(m)) {
                        p.remove(m);
                    }
                }
            }
        }
    }

    private static void deleteEmptyLists(LinkedList<LinkedList<Integer>> prime, LinkedList<String> expression,
                                         LinkedList<String> essentialExpresion) {
        for (int i = 0; i < prime.size(); i++) {
            if (prime.get(i).isEmpty()) {
                prime.remove(i);
                expression.remove(i);
                i--;
            }
        }
    }

    public void getEssential(LinkedList<LinkedList<Integer>> prime, LinkedList<LinkedList<Integer>> essentials,
                             LinkedList<String> essentialExpresion, LinkedList<String> expression,
                             LinkedList<Integer> rowRemoved) {
        findEsential(prime, essentials, essentialExpresion, expression, rowRemoved);
        deleteCoveredMinterms(prime, essentials);
        deleteEmptyLists(prime, expression, essentialExpresion);
    }

    public void columnDom(LinkedList<String>[] arr, LinkedList<Integer> removedColumn) {
        int i, j;
        for (i = 0; i < arr.length - 1; i++) {
            if (arr[i].size() == 0) {
                while (i < arr.length - 1 && arr[i].size() == 0) {
                    i++;
                }
            }
            for (j = i + 1; j < arr.length; j++) {
                if (arr[j].size() == 0) {
                    while (j < arr.length && arr[j].size() == 0) {
                        j++;
                    }
                    if (j == arr.length)
                        break;
                }
                if (arr[i].containsAll(arr[j])) {
                    arr[i].clear();
                    removedColumn.add(i);
                    System.out.println("lol");
                    break;
                } else if (arr[j].containsAll(arr[i])) {
                    removedColumn.add(j);
                    System.out.println("lol");
                    arr[j].clear();
                }
            }
        }
    }

    public void rowDom(LinkedList<LinkedList<Integer>> prime, LinkedList<String> expression,
                       LinkedList<LinkedList<Integer>> removedRows, LinkedList<String> removedRowsExpression) {

        for (int i = 0; i < prime.size() - 1; i++) {
            for (int j = i + 1; j < prime.size(); j++) {
                if (prime.get(i).containsAll(prime.get(j)) && (prime.get(i).size() != prime.get(j).size())) {

                    removedRows.add(prime.get(j));
                    removedRowsExpression.add(expression.get(j));

                    prime.remove(j);
                    expression.remove(j);
                    j--;
                } else if (prime.get(j).containsAll(prime.get(i)) && (prime.get(i).size() != prime.get(j).size())) {

                    removedRows.add(prime.get(i));
                    removedRowsExpression.add(expression.get(i));

                    prime.remove(i);
                    expression.remove(i);
                    i--;
                    break;
                }
            }
        }
    }

    public void patrik(LinkedList<String>[] arr, int start, int end, LinkedList<LinkedList<String>> all,
                       LinkedList<String> expression) {

        boolean added = false;
        for (String str : arr[start]) {
            if (!expression.contains(str)) {
                expression.add(str);
                added = true;
            }

            if (start < end - 1 && arr[start + 1].size() == 0) {
                while (start < end - 1 && arr[start + 1].size() == 0) {
                    start++;
                }
            }

            if (start < end - 1) {
                patrik(arr, start + 1, end, all, expression);
            } else {
                LinkedList<String> branch = new LinkedList<>();
                branch = (LinkedList) expression.clone();
                all.add(branch);
            }

            if (added) {
                expression.removeLast();
            }
            added = false;
        }
    }

    public void removeDublicatesAfterPatrick(LinkedList<LinkedList<String>> allPossibles) {
        int counter = 0;
        for (int i = 0; i < allPossibles.size(); i++) {
            counter = 0;
            for (int j = 0; j < allPossibles.size(); j++) {
                if (allPossibles.get(i).containsAll(allPossibles.get(j)) && (allPossibles.get(i).size() == allPossibles.get(j).size())) {
                    counter++;
                }
            }
            if (counter > 1) {
                allPossibles.remove(i);
                i--;
            }
        }
    }

    public String fromListToString(LinkedList<String> expressionList) {
        StringBuilder stringExpression = new StringBuilder();
        for (int i = 0; i < expressionList.size(); i++) {
            if (expressionList.get(i).length() > 0) {
                stringExpression.append(expressionList.get(i));
                if (i != expressionList.size() - 1) {
                    stringExpression.append("+ ");
                }
            }
        }
        return stringExpression.toString();
    }

    public void getMinimizedForm(LinkedList<LinkedList<String>> allPossibleExpressions, String essentialExpression,
                                 LinkedList<String> finalResult) {
        if (!allPossibleExpressions.isEmpty()) {

            int min = allPossibleExpressions.getFirst().size();
            for (LinkedList<String> p : allPossibleExpressions) {
                if (p.size() == min) {

                    StringBuilder s = new StringBuilder();
                    s.append(essentialExpression);
                    if (!p.isEmpty() && essentialExpression.length() != 0) {
                        s.append(" + ");
                    }
                    s.append(fromListToString(p));
                    finalResult.add(s.toString());

                } else if (p.size() <= min) {

                    finalResult.clear();
                    min = p.size();
                    StringBuilder s = new StringBuilder();
                    s.append(essentialExpression);
                    if (!p.isEmpty() && essentialExpression.length() != 0) {
                        s.append(" + ");
                    }
                    s.append(fromListToString(p));
                    finalResult.add(s.toString());

                }
            }
        } else {
            finalResult.add(essentialExpression);
        }
    }
}
