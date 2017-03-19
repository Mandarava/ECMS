package com.finance.exercise.producer;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zt on 2017/3/16.
 */
public abstract class AbstractProducer {

    private static CircleQueue circleQueue = new CircleQueue(20);
    private Timer timer;

    public CircleQueue getCircleQueue() {
        return circleQueue;
    }

    public void startTimer() {
        if (timer == null) {
            timer = new Timer();
        } else {
            return;
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int times = new Random().nextInt(5) + 1;
                for (int i = 0; i < times; i++) {
                    int number = new Random().nextInt(1000);
                    circleQueue.add(number);
                    setCache(number);
                }
            }
        }, 0, 5 * 1000);
    }

    public abstract void setCache(int number);

}
