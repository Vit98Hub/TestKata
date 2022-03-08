package Calculator;

import java.util.*;

public class Calculator {
    private static final String[] romanNumbers = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
    private static final ConvertNumbers convertNumbers = new ConvertNumbers();

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String result;
        while (true) {
            System.out.println("Введите выражение:");
            String userInput = scanner.nextLine();
            result = checkValidityExpression(userInput);
            System.out.println(result + "\n");
            ConvertNumbers.result = "";
        }
    }

    private static String checkValidityExpression(String userInput) {
        int number1;
        int number2;
        String operator;
        int result;
        String[] expression = userInput.split(" ");
        try {
            if (expression.length != 3) {throw new ExpressionLength();}
            operator = expression[1];
            if (operator.matches("[^-+*/]")) {throw new OperatorException();}
            if (expression[0].matches("[1-9]|(10)") & expression[2].matches("[1-9]|(10)")) {
                number1 = Integer.parseInt(expression[0]);
                number2 = Integer.parseInt(expression[2]);
                result = Calculation.start(number1, number2, operator);
                return Integer.toString(result);
            } else if (Arrays.asList(romanNumbers).contains(expression[0]) &
                    Arrays.asList(romanNumbers).contains(expression[2])) {
                number1 = (Arrays.asList(romanNumbers).indexOf(expression[0])) + 1;
                number2 = (Arrays.asList(romanNumbers).indexOf(expression[2])) + 1;
                if (Objects.equals(operator, "-")) {
                    if (number1 - number2 <= 0) {throw new NegativeRomanNumber();}
                }
                if (Objects.equals(operator, "/")) {
                    if (number1 / number2 == 0) {throw new NegativeRomanNumber();}
                }
                result = Calculation.start(number1, number2, operator);
                return convertNumbers.convertArabicNumToRoman(result);
            } else {throw new NumberFormatException("Операции проводятся только над числами 1-10 или I-X " +
                    "одновременно");}
        } catch (OperatorException | ExpressionLength | NumberFormatException | NegativeRomanNumber error) {
            return error.getMessage();
        }
    }
}

class ConvertNumbers {

    private static final TreeMap<Integer, String> romanNumbers;
    static {
        romanNumbers = new TreeMap<>(Comparator.reverseOrder());
        romanNumbers.put(100, "C");
        romanNumbers.put(90, "XC");
        romanNumbers.put(50, "L");
        romanNumbers.put(40, "XL");
        romanNumbers.put(10, "X");
        romanNumbers.put(9, "IX");
        romanNumbers.put(5, "V");
        romanNumbers.put(4, "IV");
        romanNumbers.put(1, "I");
    }
    public static String result = "";

    public String convertArabicNumToRoman(int number) {
        int count;
        for (Object key : romanNumbers.keySet()) {
            count = number / (int)key;
            if (count != 0) {
                number -= (int)key * count;
                while (count > 0) {
                    result += romanNumbers.get(key);
                    count -= 1;
                }
            }
        }
        return result;
    }
}

class Calculation {

    public static int start(int number1, int number2, String operator) {
        return switch (operator) {
            case "-" -> number1 - number2;
            case "+" -> number1 + number2;
            case "/" -> number1 / number2;
            case "*" -> number1 * number2;
            default -> 0;
        };
    }
}

class ExpressionLength extends Exception {

    public String getMessage() {
        return "Формат математической операции не удовлетворяет условию - два операнда и один оператор (+, -, /, *)\n";
    }
}

class NegativeRomanNumber extends Exception {

    public String getMessage() {
        return "В римской системе исчисления нет отрицательных чисел и нуля";
    }
}

class OperatorException extends Exception {

    public String getMessage() {
        return "Допустимо использование одного только оператора из перечисленных (+, -, /, *)\n";
    }
}