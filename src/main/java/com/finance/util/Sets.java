package com.finance.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zt
 * 2018/1/8 22:09
 */
public class Sets {

    // 合并
    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<T>(a);
        result.addAll(b);
        return result;
    }

    // 返回两个集合共有的部分
    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<T>(a);
        result.retainAll(b);
        return result;
    }

    // subtract subset from superset 从superset中移除subset
    public static <T> Set<T> difference(Set<T> superset, Set<T> subset) {
        Set<T> result = new HashSet<T>(superset);
        result.removeAll(subset);
        return result;
    }

    // everything not in the intersection 除了交集之外的所有元素
    public static <T> Set<T> complement(Set<T> a, Set<T> b) {
        return difference(union(a, b), intersection(a, b));
    }

}
