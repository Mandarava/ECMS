package com.finance.test.delegate;

/**
 * Created by zt 2017/7/23 20:18 代理
 */
public class SpaceShipDelegation {

    private String name;

    private SpaceShipControls spaceShipControls = new SpaceShipControls();

    public SpaceShipDelegation(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        SpaceShipDelegation spaceShipDelegation = new SpaceShipDelegation("test");
        spaceShipDelegation.forward(100);
    }

    // Delegated methods
    public void up(int velocity) {
        spaceShipControls.up(velocity);
    }

    public void down(int velocity) {
        spaceShipControls.down(velocity);
    }

    public void left(int velocity) {
        spaceShipControls.left(velocity);
    }

    public void right(int velocity) {
        spaceShipControls.right(velocity);
    }

    public void forward(int velocity) {
        spaceShipControls.forward(velocity);
    }

    public void back(int velocity) {
        spaceShipControls.back(velocity);
    }

    public void turboBoost(int velocity) {
        spaceShipControls.turboBoost(velocity);
    }

}
