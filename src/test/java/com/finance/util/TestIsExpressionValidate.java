package com.finance.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by zt on 2016/10/22.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestIsExpressionValidate {

    @Test
    public void test() {
        String expression = "[sum+b*(1)*](5-4)*{x+b+b*(1+2)}";
        boolean result = isExpressionValid2(expression);
        Assert.assertTrue(result);
    }

    private boolean isExpressionValid2(String expression) {
        int length = expression.length();
        Stack<Character> s = new Stack<Character>();
        Stack<Character> s2 = new Stack<Character>();
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
                    if (s2.size() > 0) {
                        if ((ch == '+' || ch == '-' || ch == '*' || ch == '/')
                                && (s2.peek() == '+' || s2.peek() == '-' || s2.peek() == '*' || s2.peek() == '/')) {
                            flag = false;
                        }
                    }
                    s2.push(ch);
                    break;
            }
        }
        return s.isEmpty() && flag;
    }

    @Test
    public void testExpression() {
        String expression = "[sum+b*(1)](5-4)*{x+b+b*(1+2)}";
        String temp = expression.replaceAll("sum|x|b", "1");
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            String d = (String) engine.eval(temp);
            System.out.println(d);
        } catch (ScriptException ex) {
            System.out.println(ex);
        }
    }


    @Test
    public void test66() {
        String s = "2016-10-23";
        Pattern p = Pattern.compile("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})");
        Matcher m = p.matcher(s);
        if (m.find()) {
            System.out.println("year: " + m.group("year")); //年
            System.out.println("month: " + m.group("month")); //月
            System.out.println("day: " + m.group("day")); //日

            System.out.println("year: " + m.group(1)); //第一组
            System.out.println("month: " + m.group(2)); //第二组
            System.out.println("day: " + m.group(3)); //第三组
        }
        System.out.println(s.replaceAll("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})", "${day}-${month}-${year}")); //将 年-月-日 形式的日期改为 日-月-年 形式
    }

}
