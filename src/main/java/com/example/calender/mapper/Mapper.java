package com.example.calender.mapper;

import com.example.calender.exception.ResourceNotFoundException;

public interface Mapper<S, T> {
    T toDto(S model);

    S toEntity(T dto) throws ResourceNotFoundException;
}