package com.example.calender.exception;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
@Slf4j
@Setter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
@ResponseBody()
public class ResourceNotFoundException extends Exception {

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s is not found with %s: '%s' ", resourceName, fieldName, fieldValue));
        log.error("Thrown ResourceNotFoundException\t" + super.getMessage());
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;

    }
}
