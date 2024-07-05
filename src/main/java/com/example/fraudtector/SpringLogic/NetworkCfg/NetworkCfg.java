package com.example.fraudtector.SpringLogic.NetworkCfg;

import com.example.fraudtector.SpringLogic.Endpoint.Endpoint;
import com.example.fraudtector.SpringLogic.TransSpec.TransSpec;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "network_cfg")
public class NetworkCfg implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long configId;

    private String portNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "spec_id", referencedColumnName = "specId")
    private TransSpec specId;

    @JsonIgnore
    @OneToMany(mappedBy = "config", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Endpoint> endpoints;
}
