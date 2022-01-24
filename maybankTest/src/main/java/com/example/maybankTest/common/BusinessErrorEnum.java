package com.example.maybankTest.common;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum BusinessErrorEnum {
    // Invalid status errors
    PRODUCT_FAILED_CREATED(HttpStatus.BAD_REQUEST,4000, "Product failed to create."),
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST,4001, "Product not found."),
    PRODUCT_FAILED_ARCHIVE(HttpStatus.BAD_REQUEST,4003, "Product failed to archive."),
    PRODUCT_LIST_RETRIEVAL_FAILED(HttpStatus.BAD_REQUEST,4004, "Product listing failed to retrieve."),
    INCORRECT_COUNTRY_CODE_FORMAT(HttpStatus.BAD_REQUEST,4005, "Incorrect country code format."),
    PRODUCT_FAILED_UPDATE(HttpStatus.BAD_REQUEST,4006, "Product failed to update."),
    COUNTRY_DETAILS_RETRIEVAL_FAILED(HttpStatus.BAD_REQUEST,4007, "Country details failed to retrieve.");


    private HttpStatus status;
    private Integer errorCode;
    private String message;

    public Integer getErrorCode() {
        return errorCode;
    }
    public HttpStatus getHttpStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }

    public static BusinessErrorEnum valueOfErrorCode(Integer errorCode) {
        for (BusinessErrorEnum errorEnum : values()) {
            if (errorCode == errorEnum.errorCode) {
                return errorEnum;
            }
        }
        return null;
    }
}
