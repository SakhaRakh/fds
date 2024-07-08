package com.example.fraudtector.JSON.SpringLogic.TransDataAttribute;

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

public class TransDataAttributeDto {
    private String attribute;
    private String fieldTag;
    private String description;
    private Long endpointId;
    private StateType stateType;
    private Long parentId;
    private String dataType;
}