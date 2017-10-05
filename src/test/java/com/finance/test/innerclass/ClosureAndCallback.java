package com.finance.test.innerclass;

/**
 * Created by zt 2017/10/5 20:56
 * Using inner classes for callbacks
 */
interface Incrementable {
    void increment();
}

class Callee1 implements Incrementable {
    private int i = 0;

    @Override
    public void increment() {
        i++;
        System.out.println(i);
    }
}

class MyIncrement {
    static void f(MyIncrement myIncrement) {
        myIncrement.increment();
    }

    public void increment() {
        System.out.println("Other operation");
    }
}

// If your class must implement increment() in some other way, you must use an inner class;
class Callee2 extends MyIncrement {
    private int i = 0;

    @Override
    public void increment() {
        super.increment();
        i++;
        System.out.println(i);
    }

    Incrementable getCallbackReference() {
        return new Closure();
    }

    private class Closure implements Incrementable {
        @Override
        public void increment() {
            // specify outer-class method, otherwise you'd get an infinite recursion;
            Callee2.this.increment();
        }
    }
}

class Caller {
    private Incrementable callbackReference;

    Caller(Incrementable incrementable) {
        callbackReference = incrementable;
    }

    void go() {
        callbackReference.increment();
    }
}

public class ClosureAndCallback {

    public static void main(String[] args) {
        Callee1 callee1 = new Callee1();
        Callee2 callee2 = new Callee2();
        MyIncrement.f(callee2);
        Caller caller1 = new Caller(callee1);
        Caller caller2 = new Caller(callee2.getCallbackReference());
        caller1.go();
        caller1.go();
        caller2.go();
        caller2.go();
    }

}
