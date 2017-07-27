package com.finance.test.delegate;

/**
 * Created by zt 2017/7/23 20:22
 */
public class SpaceShip extends SpaceShipControls {

    private String name;

    public SpaceShip(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        SpaceShip spaceShip = new SpaceShip("test");
        spaceShip.forward(100);
    }

    @Override
    public String toString() {
        return "SpaceShip{" +
                "name='" + name + '\'' +
                '}';
    }

}
