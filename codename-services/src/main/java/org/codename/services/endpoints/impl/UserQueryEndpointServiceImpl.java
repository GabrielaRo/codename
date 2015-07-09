/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
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
    public Response getAll(Long lon, Long lat, String interests, String lookingFor, String categories) throws ServiceException {
        List<String> interestsList = null;
        if (interests != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(interests.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            interestsList = new ArrayList<String>(array.size());

            if (array != null) {

                for (int i = 0; i < array.size(); i++) {
                    log.info("Interest[" + i + "]: " + array.getString(i));

                    interestsList.add(array.getString(i));
                }

            }
        }
        List<String> categoriesList = null;
         if (categories != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(categories.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            

            if (array != null) {
                categoriesList = new ArrayList<String>(array.size());
                for (int i = 0; i < array.size(); i++) {
                    log.info("Category[" + i + "]: " + array.getString(i));

                    categoriesList.add(array.getString(i));
                }

            }
        }
         List<String> lookingForList = null;
         if (lookingFor != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(lookingFor.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            

            if (array != null) {
                lookingForList = new ArrayList<String>(array.size());
                for (int i = 0; i < array.size(); i++) {
                    log.info("Looking For[" + i + "]: " + array.getString(i));

                    lookingForList.add(array.getString(i));
                }

            }
        }
        List<User> users = usersQueryService.getAll(lon, lat, interestsList, lookingForList, categoriesList );
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (User u : users) {
            JsonObjectBuilder jsonUserObjectBuilder = createFullJsonUser(u);
            jsonArrayBuilder.add(jsonUserObjectBuilder);
        }
        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

    
   

}
