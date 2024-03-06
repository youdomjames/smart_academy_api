package com.youdomjames.course_service.exception.handler;

import com.youdomjames.course_service.HttpResponse;
import com.youdomjames.course_service.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ApiException.class})
    public ResponseEntity<Object> handleApiException(
            Exception ex) {
        log.error(
                "API EXCEPTION OCCURRED: " + ex.getMessage() + " " + ex.getClass().getSimpleName()
        );
        return ResponseEntity.status(BAD_REQUEST).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message(ex.getMessage())
                        .status(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .build()
        );
    }

    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity<Object> handleDuplicateKeyException() {
        return ResponseEntity.status(CONFLICT).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("This course is already present.")
                        .developerMessage("Try adding new one with different name, year or session")
                        .status(CONFLICT)
                        .statusCode(CONFLICT.value())
                        .build()
        );
    }
}
