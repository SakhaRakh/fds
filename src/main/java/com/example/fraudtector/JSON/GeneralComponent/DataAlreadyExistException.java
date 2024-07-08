package com.example.fraudtector.JSON.GeneralComponent;

public class DataAlreadyExistException extends Exception {
    public DataAlreadyExistException(String message) {
        super(message);
    }

    public DataAlreadyExistException() {
        super("Data already exist, please check existing one.");
    }
}