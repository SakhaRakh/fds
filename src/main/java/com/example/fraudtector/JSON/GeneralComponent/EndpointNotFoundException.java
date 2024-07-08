package com.example.fraudtector.JSON.GeneralComponent;

public class EndpointNotFoundException extends Exception {
    public EndpointNotFoundException() {
        super("Endpoint not found, please input correct one.");
    }
}
