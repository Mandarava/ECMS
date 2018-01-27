package com.finance.test.generic;

/**
 * 元组 tuple
 * Created by zt
 * 2018/1/8 21:41
 */
public class TwoTuple<A, B> {

    public final A first;
    public final B second;

    public TwoTuple(A a, B b) {
        first = a;
        second = b;
    }

    @Override
    public String toString() {
        return "TwoTuple{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}

class Tuple {

    public static <A, B> TwoTuple<A, B> tuple(A a, B b) {
        return new TwoTuple<A, B>(a, b);
    }
}
