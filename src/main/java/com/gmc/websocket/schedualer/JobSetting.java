package com.gmc.websocket.schedualer;

import org.quartz.*;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;

@Configuration
public class JobSetting {

    final WebsocketScheduler websocketScheduler;
    private final Scheduler scheduler;

    public JobSetting(WebsocketScheduler websocketScheduler, Scheduler scheduler) {
        this.websocketScheduler = websocketScheduler;
        this.scheduler = scheduler;
    }

    public void start() {
        JobDetail jobDetail = buildJobDetail(WebsocketScheduler.class, new HashMap<>());

        try {
            scheduler.scheduleJob(jobDetail, buildJobTrigger("0/2 * * * * ?"));

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public Trigger buildJobTrigger(String scheduleExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
    }

    public JobDetail buildJobDetail(Class job, Map params) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);

        return newJob(job).usingJobData(jobDataMap).build();
    }
}
