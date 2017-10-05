package com.finance.test.innerclass;

/**
 * Created by zt 2017/10/5 20:39
 * @See Thinking in Java 4th P205
 */
public class MultiImplementation {

    static void takesD(D d) { }

    static void takesE(E e) { }

    public static void main(String[] args) {
        Z z = new Z();
        takesD(z);
        takesE(z.makeE());
    }
}

class D { }

abstract class E { }

class Z extends D {
    E makeE() {
        return new E() {};
    }
}
