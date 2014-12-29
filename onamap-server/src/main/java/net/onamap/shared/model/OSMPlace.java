package net.onamap.shared.model;

import com.googlecode.objectify.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Data
@Cache
public class OSMPlace implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long osm_id;

    @Embedded
    private OSMAddress address;

    @Index
    private Double lat;

    @Index
    private Double lon;

    @Index
    private Long place_id;

    @Data
    @NoArgsConstructor
    @Embed
    public static class OSMAddress implements Serializable {
        /**
         * Default Serialization UID
         */
        private static final long serialVersionUID = 1L;
        private String city;
        private String country;
        private String state;
    }

    @Override
    public int hashCode() {
        return osm_id.intValue();
    }

}
