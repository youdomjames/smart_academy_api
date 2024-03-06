package com.youdomjames.student_service.form;

import lombok.Builder;
import lombok.Data;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-02-22
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Data
@Builder
public class AddressForm {
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String apartmentNumber;
}
