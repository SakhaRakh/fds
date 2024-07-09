package com.example.fraudtector.ISO8583.SpringLogic.FieldConfiguration;

import com.example.fraudtector.ISO8583.SpringLogic.SchemeConfiguration.SchemeConfiguration;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "iso_field_configuration")
public class FieldConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public int fieldId;

    public int length;

    public String dataType;

    private Boolean pad = false;

    private Boolean padChar;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "scheme_configuration_id", referencedColumnName = "id")
    private SchemeConfiguration schemeConfiguration;

    /*
        field id: 52
        length: 8
        dataType: CHAR
        format: BINARY

        field id: 52
        length: 8
        dataType: BINARY
        format: ASCII

        field id: 2
        length: 19
        dataType: LLVAR
        format: ASCII

        LLNUM -> semua field harus angka
        LLCHAR -> semua field harus character

        LLNUM -> 00000000 dan ini mulai dari kiri
        LLCHAR -> space dan ini mulai dari kanan
     */
}
