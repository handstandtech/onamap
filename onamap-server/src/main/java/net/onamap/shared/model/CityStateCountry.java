package net.onamap.shared.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CityStateCountry implements Serializable {
    /**
     * Default Serialization UID
     */
    private static final long serialVersionUID = 1L;
    String city;
    String country;
    String state;
}