package com.freela.service.constants;

import com.freela.exception.ApiException;
import com.freela.service.validator.ApiUserValidator;

public class ApiExceptionConstants {
    public static final ApiException.Source INVALID_PASSWORD = new ApiException.Source(
            ApiException.Location.BODY,
            "password",
            "***",
            "Valid password");
    public static final ApiException.Source EMPTY_OR_NULL_PASSWORD = new ApiException.Source(
            ApiException.Location.BODY,
            "password",
            "Empty or null",
            "Valid password");
    public static final ApiException.Source EMPTY_OR_NULL_ROLE_IDS = new ApiException.Source(
            ApiException.Location.BODY,
            "roleIds",
            "Empty or null",
            "Valid list of Role IDs");
    public static final ApiException.Source EMPTY_SEARCH_CRITERIA = new ApiException.Source(
            ApiException.Location.QUERY,
            "[email, name]",
            "Empty or null",
            "Valid search criteria"
    );
    public static ApiException.Source getUnregisteredEmail(String userEmail){
        return new ApiException.Source(
                ApiException.Location.BODY,
                "email",
                 "%s".formatted(userEmail),
                "Unregistered email"
        );
    }
}