package com.finance.exercise.producer;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

/**
 * Created by zt on 2017/3/18.
 */
@Component
public class Producer extends AbstractProducer {

    private static Map<HttpSession, List<Integer>> datas = new ConcurrentHashMap<>();
    private HttpSession session;

    public Producer() {

    }

    public Producer(HttpSession session) {
        this.session = session;
    }

    @Override
    public void setCache(int number) {
        if (datas != null) {
            for (Map.Entry<HttpSession, List<Integer>> entry : datas.entrySet()) {
                List<Integer> list = entry.getValue();
                if (CollectionUtils.isEmpty(list)) {
                    list = new Vector<>();
                }
                list.add(number);
                entry.setValue(list);
            }
        }
    }

    public int[] getIncrementalData() {
        System.out.println(this.hashCode());
        List<Integer> list = datas.get(session);
        datas.put(session, new Vector<>());
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

}
