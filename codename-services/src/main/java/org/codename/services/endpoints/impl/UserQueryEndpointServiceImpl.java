/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import org.codename.core.api.UsersQueryService;

import org.codename.model.User;


import org.codename.services.endpoints.api.UserQueryEndpointService;
import static org.codename.services.endpoints.impl.UsersHelper.createFullJsonUser;
import org.codename.core.exceptions.ServiceException;

/**
 *
 * @author grogdj
 */
@Stateless
public class UserQueryEndpointServiceImpl implements UserQueryEndpointService {

    @Inject
    private UsersQueryService usersQueryService;

    private final static Logger log = Logger.getLogger(UserQueryEndpointServiceImpl.class.getName());

    private final String UPLOADED_FILE_PARAMETER_NAME = "file";

    public UserQueryEndpointServiceImpl() {

    }

    @Override
    public Response getAll() throws ServiceException {
        List<User> users = usersQueryService.getAll();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (User u : users) {
            JsonObjectBuilder jsonUserObjectBuilder = createFullJsonUser(u);
            jsonArrayBuilder.add(jsonUserObjectBuilder);
        }
        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

}
