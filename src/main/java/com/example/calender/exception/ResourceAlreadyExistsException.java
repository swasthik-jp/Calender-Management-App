package com.example.calender.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@NoArgsConstructor
@Slf4j
@Setter
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class ResourceAlreadyExistsException extends Exception{
    private String resourceName;
    private String fieldName;
    private Object fieldValue;
    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: '%s' ",resourceName,fieldName,fieldValue));
        log.error("thrown resource already exists exception\t" + super.getMessage());
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
