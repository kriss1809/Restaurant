package com.example.seminar1314finalizat.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;

}