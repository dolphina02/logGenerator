package com.example.weblogproducer.config;

import com.example.weblogproducer.RW.OrderLogItemReader;
import com.example.weblogproducer.RW.OrderLogItemWriter;
import com.example.weblogproducer.RW.WebLogItemReader;
import com.example.weblogproducer.RW.WebLogItemWriter;
import com.example.weblogproducer.entity.OrderLog;
import com.example.weblogproducer.entity.WebLog;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final KafkaTemplate<String, WebLog> webLogKafkaTemplate;

    private final KafkaTemplate<String, OrderLog> orderLogKafkaTemplate;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public BatchConfig(KafkaTemplate<String, WebLog> webLogKafkaTemplate, KafkaTemplate<String, OrderLog> orderLogKafkaTemplate, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.webLogKafkaTemplate = webLogKafkaTemplate;
        this.orderLogKafkaTemplate = orderLogKafkaTemplate;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    // H2 데이터베이스를 설정합니다.
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("password");
//        return dataSource;
//    }

    // WebLogItemReader를 빈으로 정의하여 Spring 컨텍스트에 등록합니다.
    // ItemReader는 배치 처리에서 데이터를 읽어오는 역할을 합니다.
    @Bean
    @StepScope
    public WebLogItemReader webLogReader() {
        return new WebLogItemReader();
    }

    // WebLogItemWriter를 빈으로 정의하여 Spring 컨텍스트에 등록합니다.
    // ItemWriter는 배치 처리에서 데이터를 쓰는 역할을 합니다.
    @Bean
    public WebLogItemWriter webLogWriter() {
        return new WebLogItemWriter(webLogKafkaTemplate);
    }

    // WebLogItemReader를 빈으로 정의하여 Spring 컨텍스트에 등록합니다.
    // ItemReader는 배치 처리에서 데이터를 읽어오는 역할을 합니다.
    @Bean
    @StepScope
    public OrderLogItemReader orderLogReader() {
        return new OrderLogItemReader();
    }

    // WebLogItemWriter를 빈으로 정의하여 Spring 컨텍스트에 등록합니다.
    // ItemWriter는 배치 처리에서 데이터를 쓰는 역할을 합니다.
    @Bean
    public OrderLogItemWriter orderLogWriter() {
        return new OrderLogItemWriter(orderLogKafkaTemplate);
    }

    // JobBuilder를 사용하여 importUserJob이라는 이름의 배치 작업을 생성합니다.
    // Job은 배치 처리의 단위 작업을 정의하는 구성 요소입니다.
    // 이 작업은 step 2개을 포함하며, RunIdIncrementer를 통해 매번 실행 시 고유한 ID를 가집니다.
    @Bean
    public Job logGenerationJob() {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(makeWebLogAndProduceStep())
                .next(makeOrderLogAndProduceStep()) // 두 번째 스텝 추가
                .build();
    }

    // StepBuilder를 사용하여 step1이라는 이름의 배치 단계를 생성합니다.
    // Step은 Job의 하위 단위로, 실제 배치 처리를 정의하는 구성 요소입니다.
    // 이 단계는 reader와 writer를 사용하여 웹 로그를 처리합니다.
    // chunk(10)은 한 번에 10개의 항목을 처리함을 의미합니다.
    @Bean
    public Step makeWebLogAndProduceStep() {
        return new StepBuilder("makeWebLogAndProduceStep", jobRepository)
                .<WebLog, WebLog>chunk(10, transactionManager)
                .reader(webLogReader())
                .writer(webLogWriter())
                .build();
    }

    @Bean
    public Step makeOrderLogAndProduceStep() {
        return new StepBuilder("makeOrderLogAndProduceStep", jobRepository)
                .<OrderLog, OrderLog>chunk(10, transactionManager)
                .reader(orderLogReader())
                .writer(orderLogWriter())
                .build();
    }
}
