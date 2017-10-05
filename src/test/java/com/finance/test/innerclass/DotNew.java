package com.finance.test.innerclass;

/**
 * Created by zt 2017/10/5 20:46
 */
public class DotNew {

    public static void main(String[] args) {
        DotNew dotNew = new DotNew();
        DotNew.Inner dni = dotNew.new Inner();
    }

    public class Inner {

    }

}
