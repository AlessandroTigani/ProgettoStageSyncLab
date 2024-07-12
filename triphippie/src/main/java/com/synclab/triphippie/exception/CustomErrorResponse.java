package com.synclab.triphippie.exception;

import java.util.Date;

public class CustomErrorResponse {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public CustomErrorResponse(Date timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }
    public CustomErrorResponse(){}

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // Costruttori, getter e setter
}
