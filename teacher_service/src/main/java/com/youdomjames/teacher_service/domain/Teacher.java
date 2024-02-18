package com.youdomjames.teacher_service.domain;

import com.youdomjames.teacher_service.enumeration.Default;
import com.youdomjames.teacher_service.enumeration.Gender;
import com.youdomjames.teacher_service.enumeration.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate hiredDate;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @Column(unique = true)
    private String email;
    private String phoneNumber;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @Column(columnDefinition = "TEXT")
    private String aboutMe;
    private String highestDegree;
    private BigDecimal salary;
    private String profilePictureLink;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    @Builder.Default
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "courseIds", joinColumns = @JoinColumn(name = "teacher_id"))
    @Column(name = "courseId", nullable = false)
    private List<String> courseIds = new ArrayList<>();
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
