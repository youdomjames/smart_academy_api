package com.youdomjames.student_service.resource;

import com.youdomjames.student_service.dto.HttpResponse;
import com.youdomjames.student_service.service.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-23
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@RestController
@RequestMapping("{profileId}/payments")
public record PaymentResource(PaymentService paymentService) {
    //TODO: Restrict access to connected user with same profileId only
    @PostMapping("/checkout")
    public ResponseEntity<HttpResponse> createCheckout(@PathVariable String profileId, @RequestParam Set<String> courseIds) {
        return ResponseEntity.status(CREATED).body(paymentService.checkOut(profileId, courseIds));
    }

    @PostMapping("/{paymentIntentSecret}/success")
    public ResponseEntity<HttpResponse> handleSuccessfulPayment(@PathVariable String profileId, @PathVariable String paymentIntentSecret) {
        return ResponseEntity.status(CREATED).body(paymentService.handleSuccessfulPayment(profileId, paymentIntentSecret));
    }

}
