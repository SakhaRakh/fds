package com.example.fraudtector.JSON.SpringLogic.FieldConfiguration;

import com.example.fraudtector.JSON.Constant.StateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DataAttrDto
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FieldConfigurationDto {
    private String attribute;
    private String fieldTag;
    private String description;
    private Long endpointId;
    private StateType stateType;
    private Long parentId;
    private String dataType;
}