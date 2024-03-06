package com.youdomjames.teacher_service.dto;

import com.youdomjames.teacher_service.enumeration.Gender;
import com.youdomjames.teacher_service.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate hiredDate;
    private Status status;
    private String email;
    private String phoneNumber;
    private Gender gender;
    private String aboutMe;
    private String highestDegree;
    private BigDecimal salary;
    private String profilePictureLink;
    private AddressDTO address;
    private Set<String> courseIds;
    private Set<PaymentDTO> payments;
}
