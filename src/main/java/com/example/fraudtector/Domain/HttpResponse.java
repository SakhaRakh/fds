package com.example.fraudtector.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.util.Date;

public class HttpResponse<T> {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss", timezone = "Asia/Jakarta")
    String timeStamp = new Date().toString();
    int responseCode;

    HttpStatus httpStatus;
    String responseReason;
    String responseMessage;
    T responseData;

    public HttpResponse(int responseCode, HttpStatus httpStatus, String responseReason, String responseMessage) {
        this.responseCode = responseCode;
        this.httpStatus = httpStatus;
        this.responseReason = responseReason;
        this.responseMessage = responseMessage;
    }

    public HttpResponse(String responseMessage, int responseCode, String responseReason, T responseData) {
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
        this.responseReason = responseReason;
        this.responseData = responseData;
    }

    public HttpResponse(String responseMessage, int responseCode, String responseReason) {
        Date timeStamp = new Date();
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
        this.responseReason = responseReason;
        this.responseData = null;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseReason() {
        return responseReason;
    }

    public T getResponseData() {
        return responseData;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "{" +
                "timeStamp='" + timeStamp + '\'' +
                ", responseCode=" + responseCode +
                ", httpStatus=" + httpStatus +
                ", responseReason='" + responseReason + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                ", responseData=" + responseData +
                '}';
    }
}

