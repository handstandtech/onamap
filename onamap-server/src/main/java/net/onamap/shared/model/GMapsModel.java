package net.onamap.shared.model;

import com.google.maps.onamapmodels.GeocodingResult;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
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
    Double lat;

    @Index
    Double lng;

    GeocodingResult geocodingResult;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
