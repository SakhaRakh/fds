package com.example.fraudtector.GeneralComponent;

public class DataNotFoundWhenUpdate extends Exception {
    public DataNotFoundWhenUpdate(Long id) {
        super(
                String.format(
                        "Data with id %s not found in backend, cancelling update",
                        id
                )
        );
    }
}

