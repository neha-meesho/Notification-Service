package com.example.demo.responses;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse{

    private ErrorDetails error;

    public ErrorResponse() {
    }

    public ErrorResponse(ErrorDetails error) {
        this.error = error;
    }

    public ErrorDetails getError() {
        return error;
    }

    public void setError(ErrorDetails error) {
        this.error = error;
    }

    public static class ErrorDetails {

        private String code;
        private String message;

        public ErrorDetails() {
        }

        public ErrorDetails(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

