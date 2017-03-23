package com.finance.exercise.producer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by zt on 2017/3/16.
 * 有一台设备在不停地产生数据，需要在web界面上实时展示这些数据 - 会有多人在各自的浏览器里查看
 */
@Controller
@RequestMapping(value = "/produce")
public class ProducerController {

    @GetMapping("/data")
    @ResponseBody
    public int[] getDataProduced(HttpSession session) {
        Producer producer = Producer.getInstance(session.getId());
        return producer.getIncrementalData();
    }

    @PostMapping("/redirect")
    public String redirectToProducer() {
        return "producer";
    }

}
