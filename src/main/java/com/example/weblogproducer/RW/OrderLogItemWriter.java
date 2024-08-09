package com.example.weblogproducer.RW;


import com.example.weblogproducer.entity.OrderLog;
import com.example.weblogproducer.entity.WebLog;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.kafka.core.KafkaTemplate;

public class OrderLogItemWriter implements ItemWriter<OrderLog> {

    private final KafkaTemplate<String, OrderLog> kafkaTemplate;

    public OrderLogItemWriter(KafkaTemplate<String, OrderLog> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void write(Chunk<? extends OrderLog> items) throws Exception {
        for (OrderLog orderLog : items) {
            kafkaTemplate.send("orderLog", orderLog);
        }
    }
}