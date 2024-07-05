package com.example.fraudtector.SpringLogic.Endpoint;

import com.example.fraudtector.SpringLogic.NetworkCfg.NetworkCfg;
import com.example.fraudtector.SpringLogic.TransSpec.TransSpec;
import lombok.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.fraudtector.SpringLogic.TransDataAttribute.TransDataAttribute;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "endpoint")
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
    private List<TransDataAttribute> dataAttributes;



}
