package com.youdomjames.teacher_service.service.kafka;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-20
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
public interface ConsumerService {
    void receive(String record);
}
