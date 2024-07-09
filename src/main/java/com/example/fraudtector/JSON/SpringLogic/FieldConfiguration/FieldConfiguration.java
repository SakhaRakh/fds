package com.example.fraudtector.JSON.SpringLogic.FieldConfiguration;

import com.example.fraudtector.JSON.Constant.StateType;
import com.example.fraudtector.JSON.SpringLogic.Endpoint.Endpoint;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "json_field_configuration")
public class FieldConfiguration implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attrId;

    private String attribute;
    private String fieldTag;
    private String description;

    @Enumerated(EnumType.STRING)
    private StateType stateType;

    private Long parentId;
    private String dataType;

    @ManyToOne
    @JoinColumn(name = "endpoint_id", referencedColumnName = "endpointId")
    private Endpoint endpoint;
}
