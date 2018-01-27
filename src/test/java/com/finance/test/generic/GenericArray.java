package com.finance.test.generic;

/**
 * Created by zt
 * 2018/1/9 22:58
 */
public class GenericArray<T> {

    private Object[] array;

    public GenericArray(int size) {
        array = new Object[size];
    }

    public static void main(String[] args) {
        GenericArray<Integer> genericArray = new GenericArray<>(10);
        for (int i = 0; i < 10; i++) {
            genericArray.put(i, i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(genericArray.get(i) + "");
        }
        try {
            Integer[] ia = genericArray.rep();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return (T) array[index];
    }

    public T[] rep() {
        return (T[]) array; // Warning: unchecked cast
    }

}
