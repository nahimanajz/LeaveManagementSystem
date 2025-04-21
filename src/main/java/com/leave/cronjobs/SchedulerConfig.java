package com.leave.cronjobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.leave.service.LeaveAccrualService;

@Configuration
@EnableScheduling

public class SchedulerConfig {
    @Autowired
    private LeaveAccrualService leaveAccrualService;

    @Scheduled(cron = "0 0 0 1 * ?") // Runs at midnight on the 1st of every month
    public void scheduleLeaveAccrual() {
        leaveAccrualService.autoAccrueLeave();
    }
   
    @Scheduled(cron = "0 0 0 31 12 ?") // Runs at midnight on December 31st
    public void scheduleCarryForward() {
       leaveAccrualService.carryForwardLeave();
    }

}
