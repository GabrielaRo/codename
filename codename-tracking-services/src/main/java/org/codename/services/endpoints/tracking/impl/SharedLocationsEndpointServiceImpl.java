/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.tracking.impl;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.codename.core.exceptions.ServiceException;
import org.codename.core.tracking.api.ShareLocationService;
import org.codename.services.endpoints.tracking.api.SharedLocationsEndpointService;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import org.codename.model.tracking.SharedLocation;

/**
 *
 * @author grogdj
 */
@Stateless
public class SharedLocationsEndpointServiceImpl implements SharedLocationsEndpointService {

    private final static Logger log = Logger.getLogger(SharedLocationsEndpointServiceImpl.class.getName());

    @Inject
    private ShareLocationService shareLocationService;

    public SharedLocationsEndpointServiceImpl() {

    }


    @Override
    public Response shareLocation(Long userID, Double latitude, Double longitude, String description) throws ServiceException {
        shareLocationService.shareLocation(userID, latitude, longitude, description);
        return Response.ok().build();
    }

    @Override
    public Response getSharedLocations(Long userId) throws ServiceException {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        List<SharedLocation> locations = shareLocationService.getSharedLocations(userId);
        for (SharedLocation l : locations) {
            JsonObjectBuilder jsonUserObjectBuilder = createJsonSharedLocation(l);
            jsonArrayBuilder.add(jsonUserObjectBuilder);
        }

        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

    private JsonObjectBuilder createJsonSharedLocation(SharedLocation l) {

        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("id", l.getId());
        jsonObjBuilder.add("userId", l.getUserId());
        jsonObjBuilder.add("desc", l.getDescription());
        jsonObjBuilder.add("lat", l.getLatitude());
        jsonObjBuilder.add("lon", l.getLongitude());
        jsonObjBuilder.add("time", l.getTimestamp().getTime());

        return jsonObjBuilder;
    }

}
