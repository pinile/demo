package ru.stepchenkov.api;

public record ApiResponse<T, E>(
        int status,
        T data,
        E error
) {}