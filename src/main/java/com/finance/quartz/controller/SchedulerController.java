package com.finance.quartz.controller;

import com.finance.model.vo.BaseResponse;
import com.finance.quartz.ScheduleJob;
import com.finance.quartz.service.ScheduleJobService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.annotation.Resource;

/**
 * Created by zt 2017/9/29 22:28
 */
@Controller
@RequestMapping(value = "/scheduler")
public class SchedulerController {

    @Resource
    private ScheduleJobService scheduleJobService;

    @RequestMapping
    public String redirectToScheduler() {
        return "scheduler";
    }

    @GetMapping("/list")
    @ResponseBody
    public BaseResponse<List<ScheduleJob>> findScheduleJobs() {
        List<ScheduleJob> jobList = scheduleJobService.findAllScheduleJobs();
        BaseResponse<List<ScheduleJob>> response = new BaseResponse<>();
        response.setData(jobList);
        return response;
    }

    @PostMapping("/add")
    @ResponseBody
    public void addJob(ScheduleJob job) {
        scheduleJobService.addJob(job);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public void deleteJob(ScheduleJob job) {
        scheduleJobService.deleteJob(job);
    }

    @PostMapping("/unschedule")
    @ResponseBody
    public void unScheduleJob(ScheduleJob job) {
        scheduleJobService.unScheduleJob(job);
    }

    @PostMapping("/reschedule")
    @ResponseBody
    public void rescheduleJob(ScheduleJob job) {
        scheduleJobService.rescheduleJob(job);
    }

    @PostMapping("/trigger")
    @ResponseBody
    public void triggerJob(ScheduleJob job) {
        scheduleJobService.triggerJob(job);
    }

    @PostMapping("/pause")
    @ResponseBody
    public void pauseTrigger(ScheduleJob job) {
        scheduleJobService.pauseTrigger(job);
    }

    @PostMapping("/resume")
    @ResponseBody
    public void resumeTrigger(ScheduleJob job) {
        scheduleJobService.resumeTrigger(job);
    }

}
