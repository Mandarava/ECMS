package com.finance.exercise.producer;

/**
 * Created by zt on 2017/3/16.
 * 用java int[] 实现一个环形(Circle) 数据结构，环的容量固定（环满后再追加会覆盖旧数据）
 */
public class CircleQueue<T> {

    private int[] circle;

    private int index = 0;

    private int CAPACITY = 100;

    public CircleQueue() {
        this.circle = new int[CAPACITY];
    }

    public CircleQueue(int size) {
        this.CAPACITY = size;
        this.circle = new int[CAPACITY];
    }

    public static void main(String[] args) {
        CircleQueue queue = new CircleQueue(20);
        for (int i = 0; i < 20; i++) {
            queue.add(i);
        }
        System.out.println(queue.toString());
    }

    public void add(int value) {
        if (circle == null) {
            return;
        }
        if (index >= CAPACITY) {
            int[] newCircle = new int[CAPACITY];
            System.arraycopy(circle, 1, newCircle, 0, CAPACITY - 1);
            newCircle[CAPACITY - 1] = value;
            circle = newCircle;
        } else {
            circle[index] = value;
            index++;
        }
    }

    public int[] getCircle() {
        int[] result = new int[index];
        System.arraycopy(circle, 0, result, 0, index);
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (circle != null) {
            for (int i = 0; i < circle.length; i++) {
                if (i != 0) {
                    sb.append(",");
                }
                sb.append(circle[i]);
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
