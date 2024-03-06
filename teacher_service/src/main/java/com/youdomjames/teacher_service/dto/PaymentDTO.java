package com.youdomjames.teacher_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-20
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Data
public class PaymentDTO {
    private BigDecimal amount;
    private LocalDateTime paymentDate;
}
