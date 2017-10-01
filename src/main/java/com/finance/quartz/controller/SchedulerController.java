package com.finance.quartz.controller;

import com.finance.enums.ResponseCodeEnum;
import com.finance.model.vo.BaseResponse;
import com.finance.quartz.ScheduleJob;
import com.finance.quartz.service.ScheduleJobService;
import com.finance.util.ResponseUtil;

import org.springframework.stereotype.Controller;
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
        return ResponseUtil.success(jobList);
    }

    @PostMapping("/add")
    @ResponseBody
    public BaseResponse addJob(ScheduleJob job) {
        boolean isSuccess = scheduleJobService.addJob(job);
        if (isSuccess) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error(ResponseCodeEnum.ERROR.getCode());
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public BaseResponse deleteJob(ScheduleJob job) {
        boolean isSuccess = scheduleJobService.deleteJob(job);
        if (isSuccess) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error(ResponseCodeEnum.ERROR.getCode());
        }
    }

    @PostMapping("/unschedule")
    @ResponseBody
    public BaseResponse unScheduleJob(ScheduleJob job) {
        boolean isSuccess = scheduleJobService.unScheduleJob(job);
        if (isSuccess) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error(ResponseCodeEnum.ERROR.getCode());
        }
    }

    @PostMapping("/reschedule")
    @ResponseBody
    public BaseResponse rescheduleJob(ScheduleJob job) {
        boolean isSuccess = scheduleJobService.rescheduleJob(job);
        if (isSuccess) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error(ResponseCodeEnum.ERROR.getCode());
        }
    }

    @PostMapping("/trigger")
    @ResponseBody
    public BaseResponse triggerJob(ScheduleJob job) {
        boolean isSuccess = scheduleJobService.triggerJob(job);
        if (isSuccess) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error(ResponseCodeEnum.ERROR.getCode());
        }
    }

    @PostMapping("/pause")
    @ResponseBody
    public BaseResponse pauseTrigger(ScheduleJob job) {
        boolean isSuccess = scheduleJobService.pauseTrigger(job);
        if (isSuccess) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error(ResponseCodeEnum.ERROR.getCode());
        }
    }

    @PostMapping("/resume")
    @ResponseBody
    public BaseResponse resumeTrigger(ScheduleJob job) {
        boolean isSuccess = scheduleJobService.resumeTrigger(job);
        if (isSuccess) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error(ResponseCodeEnum.ERROR.getCode());
        }
    }

}
