package net.onamap.shared.model;

import com.googlecode.objectify.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Data
@Cache
public class GMapsModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    Long id;

    @Index
    String placeId;

    @Index
    Double lat;

    @Index
    Double lng;

    Double placeLat;

    Double placeLng;

    //LOCALITY
    ShortAndLongName city;

    //COUNTRY
    @Index
    ShortAndLongName country;

    //ADMINISTRATIVE_AREA_LEVEL_1
    @Index
    ShortAndLongName state;

    String formattedAddress;

    @Override
    public int hashCode() {
        return placeId.hashCode();
    }

}
