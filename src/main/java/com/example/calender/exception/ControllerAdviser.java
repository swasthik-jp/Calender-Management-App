package com.example.calender.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ControllerAdviser extends ResponseEntityExceptionHandler {
    private static final String TIME_STAMP = "timestamp";

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Object> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex,
                                                                       WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIME_STAMP, LocalDateTime.now());
        body.put("message", "Resource already exists");

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleSQLIntegrityException(SQLIntegrityConstraintViolationException ex,
                                                              WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        log.error(ex.getMessage());
        body.put(TIME_STAMP, LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                  WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIME_STAMP, LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PolicyViolationException.class)
    public ResponseEntity<Object> handlePolicyViolationException(PolicyViolationException ex,
                                                                 WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIME_STAMP, LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex,
                                                                 WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        log.error(ex.getMessage());
        body.put(TIME_STAMP, LocalDateTime.now());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIME_STAMP, LocalDate.now());
        body.put("status", status.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(x -> x.getDefaultMessage())
                .toList();

        body.put("message", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {


            Map<String, Object> body = new LinkedHashMap<>();
            log.warn(ex.getMessage());
            body.put(TIME_STAMP, LocalDateTime.now());
            body.put("message", "required request body is missing");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


}
