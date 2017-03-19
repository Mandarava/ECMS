package com.finance.exercise.producer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by zt on 2017/3/16.
 */
@Controller
@RequestMapping(value = "/produce")
public class ProducerController {

    @GetMapping("/start")
    @ResponseBody
    public String start(HttpSession session) {
        Producer producer = new Producer(session);
        producer.startTimer();
        return "success";
    }

    @GetMapping("/data")
    @ResponseBody
    public int[] getDataProduced(HttpSession session) {
        Producer producer = new Producer(session);
        producer.startTimer();
        return producer.getIncrementalData();
    }

    @PostMapping("/redirect")
    public String redirectToProducer() {
        return "producer";
    }

}
