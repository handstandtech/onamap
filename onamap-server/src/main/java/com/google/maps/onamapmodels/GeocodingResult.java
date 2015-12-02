// IntelliJ API Decompiler stub source generated from a class file
// Implementation of methods is not available

package com.google.maps.onamapmodels;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GeocodingResult {
    public List<AddressComponent> addressComponents;
    public String formattedAddress;
    public String placeId;
    public Geometry geometry;

}