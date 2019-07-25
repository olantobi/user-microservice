package com.liferon.usermgt.type;

public class RestErrorResponse {

    private final String error;
    private final String error_description;

    public RestErrorResponse(String error, String description) {
        this.error = error;
        this.error_description = description;
    }

    public String getError() {
        return this.error;
    }

    public String getError_description() {
        return this.error_description;
    }

}
