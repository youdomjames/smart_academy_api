package com.youdomjames.student_service.service.payment;

import com.youdomjames.student_service.dto.HttpResponse;
import com.youdomjames.student_service.exception.ApiException;
import com.youdomjames.student_service.form.PaymentForm;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-23
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Service
public interface PaymentService {
    HttpResponse checkOut(@NotNull String profileId, @NotNull Set<String> courseIds) throws ApiException;

    HttpResponse handleSuccessfulPayment(String profileId, String paymentIntentSecret) throws ApiException;
}
