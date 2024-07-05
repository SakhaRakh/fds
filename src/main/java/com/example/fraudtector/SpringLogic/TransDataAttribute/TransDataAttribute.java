package com.example.fraudtector.SpringLogic.TransDataAttribute;

import com.example.fraudtector.Constant.StateType;
import com.example.fraudtector.SpringLogic.Endpoint.Endpoint;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "data_attribute")
public class TransDataAttribute implements Serializable {
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
