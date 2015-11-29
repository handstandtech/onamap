package net.onamap.server.task;

import com.google.maps.onamapmodels.GeocodingResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class GMapsResponseWrapper {
    GeocodingResult[] results;

    String status;
}
