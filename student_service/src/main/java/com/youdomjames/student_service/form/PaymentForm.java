package com.youdomjames.student_service.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-23
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Data
@Builder
public class PaymentForm {
    private BigDecimal totalAmount;
    @NotNull(message = "Provide a successUrl")
    @NotEmpty
    private String successUrl;
    @NotNull(message = "Provide a cancelUrl")
    @NotEmpty
    private String cancelUrl;
    private Set<ItemForm> items;
}
