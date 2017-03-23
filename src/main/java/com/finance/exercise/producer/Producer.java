package com.finance.exercise.producer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zt on 2017/3/18.
 */
public class Producer extends AbstractProducer {

    private static Map<String, List<Integer>> datas = new HashMap<>();
    private static Map<String, Producer> instances = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    private String sessionId;

    /**
     * used to start timer
     */
    /*static {
        Producer producer = new Producer();
        producer.startTimer();
    }*/
    private Producer() {

    }

    private Producer(String sessionId) {
        this.sessionId = sessionId;
        datas.put(sessionId, new ArrayList<>());
    }

    public static Producer getInstance(String sessionId) {
        if (StringUtils.isEmpty(sessionId)) {
            return null;
        }
        if (!instances.containsKey(sessionId)) {
            Producer producer = new Producer(sessionId);
            instances.put(sessionId, producer);
            return producer;
        } else {
            return instances.get(sessionId);
        }
    }

    @Override
    public void setCache(int number) {
        lock.lock();
        try {
            for (Map.Entry<String, List<Integer>> entry : datas.entrySet()) {
                List<Integer> list = entry.getValue();
                list.add(number);
            }
        } finally {
            lock.unlock();
        }
    }

    public int[] getIncrementalData() {
        lock.lock();
        try {
            List<Integer> list = datas.get(sessionId);
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            int[] result = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                result[i] = list.get(i);
            }
            list.clear();
            return result;
        } finally {
            lock.unlock();
        }
    }

}
