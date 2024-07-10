package com.example.fraudtector.JSON.SpringLogic.Endpoint;

import com.example.fraudtector.JSON.SpringLogic.NetworkCfg.NetworkCfg;
import com.example.fraudtector.JSON.SpringLogic.FieldConfiguration.FieldConfiguration;
import com.example.fraudtector.JSON.SpringLogic.TransSpec.TransSpec;
import lombok.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "json_endpoint")
public class Endpoint implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long endpointId;

    private String url;
    private Boolean isAuth;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "spec_id", referencedColumnName = "specId")
    private TransSpec spec;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "config_id", referencedColumnName = "configId")
    private NetworkCfg config;

    @JsonIgnore
    @OneToMany(mappedBy = "endpoint", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<FieldConfiguration> dataAttributes;



}
