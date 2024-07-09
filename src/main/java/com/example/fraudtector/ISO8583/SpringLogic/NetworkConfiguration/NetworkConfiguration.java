package com.example.fraudtector.ISO8583.SpringLogic.NetworkConfiguration;

import com.example.fraudtector.ISO8583.SpringLogic.SchemeConfiguration.SchemeConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "iso_network_configuration")
public class NetworkConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int localPort;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "scheme_configuration_id", referencedColumnName = "id")
    private SchemeConfiguration schemeConfiguration;
}
