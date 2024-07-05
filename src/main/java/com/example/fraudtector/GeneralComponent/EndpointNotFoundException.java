package com.example.fraudtector.GeneralComponent;

public class EndpointNotFoundException extends Exception {
    public EndpointNotFoundException() {
        super("Endpoint not found, please input correct one.");
    }
}
