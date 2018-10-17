package Tabular;

public class Minterms {

    String expression;
    StringBuilder dontCares;

    public Minterms(String exp) {
        expression = exp;
    }

    public boolean isNumber(String expression, int i) {
        if (expression.charAt(i) <= '9' && expression.charAt(i) >= '0') {
            return true;
        } else {
            return false;
        }

    }

    public String getMinterms() {
        StringBuilder number = new StringBuilder();
        StringBuilder minterms = new StringBuilder();
        for (int i = 0; i < expression.length() && (expression.charAt(i) != 'd' || expression.charAt(i) != 'D'); i++) {
            if (isNumber(expression, i)) {
                number.append(expression.charAt(i));
            } else if (!isNumber(expression, i) && number.length() != 0) {
                minterms.append(number + " ");
                number = new StringBuilder();
            }
        }

        return minterms.toString();
    }
}
