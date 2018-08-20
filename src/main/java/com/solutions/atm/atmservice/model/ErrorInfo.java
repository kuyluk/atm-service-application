package com.solutions.atm.atmservice.model;

public class ErrorInfo {

    private String requestUrl;

    private final String message;

    public ErrorInfo(String requestUrl, String message) {
        this.requestUrl = requestUrl;
        this.message = message;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getMessage() {
        return message;
    }
}
