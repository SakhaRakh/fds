package com.example.fraudtector.JSON.SpringLogic.TransSpec;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransSpecDto {
    
    @NotEmpty(message="Name is required")
    private String name;

}