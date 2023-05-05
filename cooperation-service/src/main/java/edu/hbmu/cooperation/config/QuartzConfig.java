package edu.hbmu.cooperation.config;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail scheduleJobDetail() {
        return JobBuilder.newJob(Job.class)
                .withIdentity("scheduleJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger scheduleTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(scheduleJobDetail())
                .withIdentity("scheduleTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 8 * * ?"))// Cron表达式：每天早上8点触发
                .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobDetails(scheduleJobDetail());
        schedulerFactoryBean.setTriggers(scheduleTrigger());
        return schedulerFactoryBean;
    }
}
