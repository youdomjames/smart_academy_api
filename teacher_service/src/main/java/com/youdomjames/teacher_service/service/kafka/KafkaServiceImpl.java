package com.youdomjames.teacher_service.service.kafka;

import com.youdomjames.teacher_service.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Objects;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-18
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Slf4j
public record KafkaServiceImpl(KafkaTemplate<String, String> kafkaTemplate) implements KafkaService {

    @Override
    public void send(ProducerRecord<String, String> producerRecord) {
        kafkaTemplate.send(producerRecord).whenComplete((result, ex) -> {
            if (Objects.nonNull(ex)) {
                log.error(ex.getMessage());
                throw new ApiException("Record= " + producerRecord + " not sent to kafka broker.");
            }
            log.info("Record sent to Topic {} with Key = {}", producerRecord.topic(), producerRecord.key());
        });
    }
}
