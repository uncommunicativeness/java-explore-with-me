package ru.practicum.explorewithme.controller;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ControllerExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(Exception e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(ErrorResponse.builder()
                .status("NOT_FOUND")
                .reason(e.getClass().getSimpleName())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> conflictException(Exception e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(ErrorResponse.builder()
                .status("CONFLICT")
                .reason(e.getClass().getSimpleName())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> badRequestException(Exception e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(ErrorResponse.builder()
                .status("BAD_REQUEST")
                .reason(e.getClass().getSimpleName())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> throwableException(Exception e) {
        log.warn(e.getMessage());

        return new ResponseEntity<>(ErrorResponse.builder()
                .status("INTERNAL_SERVER_ERROR")
                .reason(e.getClass().getSimpleName())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class ErrorResponse {
        String status;
        String reason;
        String message;
        LocalDateTime timestamp;
    }
}