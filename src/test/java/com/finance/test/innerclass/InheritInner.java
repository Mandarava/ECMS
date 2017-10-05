package com.finance.test.innerclass;

/**
 * Created by zt 2017/10/5 21:43
 */
class WithInner {

    class Inner {

    }
}

public class InheritInner extends WithInner.Inner {

    InheritInner(WithInner withInner) {
        withInner.super();
    }

    public static void main(String[] args) {
        WithInner withInner = new WithInner();
        InheritInner inheritInner = new InheritInner(withInner);
    }

}
