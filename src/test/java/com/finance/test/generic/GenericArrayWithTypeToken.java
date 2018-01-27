package com.finance.test.generic;

import java.lang.reflect.Array;

/**
 * Created by zt
 * 2018/1/9 23:03
 */
public class GenericArrayWithTypeToken<T> {

    private T[] array;

    public GenericArrayWithTypeToken(Class<T> type, int size) {
        array = (T[]) Array.newInstance(type, size);
    }

    public static void main(String[] args) {
        GenericArrayWithTypeToken<Integer> genericArrayWithTypeToken = new GenericArrayWithTypeToken<>(Integer.class, 10);
        Integer[] ia = genericArrayWithTypeToken.rep();
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return array[index];
    }

    public T[] rep() {
        return array;
    }

}
