package com.example.fraudtector.ISO8583.SpringLogic.SchemeConfiguration;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "iso_scheme_configuration")
public class SchemeConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
}
