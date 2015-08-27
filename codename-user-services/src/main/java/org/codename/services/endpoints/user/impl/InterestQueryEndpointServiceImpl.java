/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.user.impl;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import org.codename.core.user.api.InterestQueryService;

import org.codename.core.exceptions.ServiceException;
import org.codename.services.endpoints.user.api.InterestQueryEndpointService;

/**
 *
 * @author grogdj
 */
@Stateless
public class InterestQueryEndpointServiceImpl implements InterestQueryEndpointService {

    @Inject
    private InterestQueryService interestQueryService;

    private final static Logger log = Logger.getLogger(InterestQueryEndpointServiceImpl.class.getName());

    public InterestQueryEndpointServiceImpl() {

    }

    public Response getAll() throws ServiceException {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        List<String> allInterests = interestQueryService.getAll();
        for (String i : allInterests) {
            JsonObjectBuilder jsonUserObjectBuilder = Json.createObjectBuilder();
            jsonUserObjectBuilder.add("text", i);
            jsonArrayBuilder.add(jsonUserObjectBuilder);
        }
        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

   

}
