package com.example.fraudtector.JSON.SpringLogic.TransSpec;

import com.example.fraudtector.JSON.SpringLogic.NetworkCfg.NetworkCfg;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import com.example.fraudtector.JSON.SpringLogic.Endpoint.Endpoint;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "json_spec")
public class TransSpec implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specId;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "specId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<NetworkCfg> networkCfgs;

    @JsonIgnore
    @OneToMany(mappedBy = "spec", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Endpoint> endpoints;
}
