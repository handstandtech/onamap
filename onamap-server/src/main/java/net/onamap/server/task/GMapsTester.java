package net.onamap.server.task;

import com.google.inject.Singleton;
import net.onamap.shared.model.GMapsModel;

import javax.ws.rs.Path;


@SuppressWarnings("serial")
@Singleton
@Path("/tasks")
public class GMapsTester {

    public static void main(String[] args) throws Exception {
        Double lat = 37.562651;
        Double lng = -77.500163;

        lat=48.719628;
        lng=-121.122797;

        GMapsModel gMapsModel = new GMaps().getGMapsModel(lat, lng);

    }


}
