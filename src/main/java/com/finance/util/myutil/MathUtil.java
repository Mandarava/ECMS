package com.finance.util.myutil;

import java.util.Arrays;
import java.util.Stack;
import java.util.function.BiFunction;

/**
 * Created by zt on 2016/10/22.
 */
public final class MathUtil {

    public static boolean isExpressionValid(String expression) {
        int length = expression.length();
        Stack<Character> s = new Stack<Character>();
        boolean flag = true;
        for (int i = 0; i < length; i++) {
            char ch = expression.charAt(i);
            switch (ch) {
                case '{':
                    s.push(ch);
                    break;
                case '}':
                    if (s.peek() == '{') {
                        s.pop();
                    } else {
                        flag = false;
                    }
                    break;
                case '[':
                    s.push(ch);
                    break;
                case ']':
                    if (s.peek() == '[') {
                        s.pop();
                    } else {
                        flag = false;
                    }
                    break;
                case '(':
                    s.push(ch);
                    break;
                case ')':
                    if (s.peek() == '(') {
                        s.pop();
                    } else {
                        flag = false;
                    }
                    break;
                default:
                    break;
            }
        }
        return s.isEmpty() && flag;
    }

    public static int evalRPN(String[] input) {
        Stack<Integer> results = new Stack<>();
        String operators = "+-*/" ;
        for (String token : input) {
            if (!operators.contains(token)) {
                results.push(Integer.valueOf(token));
                continue;
            }
            if (token.equals("+")) {
                results.push(results.pop() + results.pop());
            } else if (token.equals("-")) {
                Integer arg2 = results.pop();
                results.push(results.pop() - arg2);
            } else if (token.equals("*")) {
                results.push(results.pop() * results.pop());
            } else if (token.equals("/")) {
                Integer arg2 = results.pop();
                results.push(results.pop() / arg2);
            } else {
                // Not an operator—push the input value
                results.push(Integer.parseInt(token));
            }
        }
        return results.pop();
    }

    public static Integer calc(String input) {
        Stack<Integer> numbers = new Stack<>();
        Arrays.asList(input.split(" ")).stream().forEach(number -> {
            switch (number) {
                case "+":
                    calcSign(numbers, (n1, n2) -> n2 + n1);
                    break;
                case "-":
                    calcSign(numbers, (n1, n2) -> n2 - n1);
                    break;
                case "*":
                    calcSign(numbers, (n1, n2) -> n2 * n1);
                    break;
                case "/":
                    calcSign(numbers, (n1, n2) -> n2 / n1);
                    break;
                default:
                    numbers.push(Integer.parseInt(number));
            }
        });
        return numbers.pop();
    }

    private static Stack<Integer> calcSign(Stack<Integer> numbers, BiFunction<Integer, Integer, Integer> operation) {
        numbers.push(operation.apply(numbers.pop(), numbers.pop()));
        return numbers;
    }

    public static void main(String[] args) {
        System.out.println(evalRPN("34 4 -".split(" ")));
        System.out.println(calc("34 4 -"));
    }

}
