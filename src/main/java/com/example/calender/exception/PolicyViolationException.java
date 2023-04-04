package com.example.calender.exception;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PolicyViolationException extends RuntimeException{
    private String policyName;

    public PolicyViolationException(String policyName) {
        super(String.format("Violates the company policy: '%s' ", policyName));
        log.error("thrown policy violation exception\t" + super.getMessage());
        this.policyName = policyName;
    }
}
