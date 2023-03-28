package com.example.calender.mapper;

public interface Mapper<S, T> {
    T toDto(S model);

    S toEntity(T dto);
}