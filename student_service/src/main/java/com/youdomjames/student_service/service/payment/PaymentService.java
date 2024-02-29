package com.youdomjames.student_service.service.payment;

import com.youdomjames.student_service.dto.HttpResponse;
import com.youdomjames.student_service.exception.ApiException;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-23
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

public interface PaymentService {
    HttpResponse checkOut(@NotNull String profileId, @NotNull Set<String> courseIds) throws ApiException;

    HttpResponse handleSuccessfulPayment(String profileId, String paymentIntentSecret) throws ApiException;
}
