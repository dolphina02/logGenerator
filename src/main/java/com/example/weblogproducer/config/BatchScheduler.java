package com.example.weblogproducer.config;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class BatchScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job logGenerationJob;

    @Scheduled(fixedDelay = 10000) // 10초마다 실행 fixedRate, fixedDelay를 잘 구분하면 좋음
    public void runBatchJob() throws Exception {
        jobLauncher.run(logGenerationJob, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
    }
}