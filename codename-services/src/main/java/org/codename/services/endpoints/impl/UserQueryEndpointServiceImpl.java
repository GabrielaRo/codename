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
import javax.ws.rs.QueryParam;
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

    public UserQueryEndpointServiceImpl() {

    }

    public Response getAll(@QueryParam("interests") String interests,
            @QueryParam("lookingFors") String lookingFors,
            @QueryParam("categories") String categories) throws ServiceException {
        List<String> interestsList = null;
        if (interests != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(interests.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            if (array != null) {
                interestsList = new ArrayList<String>(array.size());
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
        if (lookingFors != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(lookingFors.getBytes()));
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
        System.out.println("Looking Fors: " + lookingForList);
        System.out.println("I am : " + categoriesList);
        System.out.println("Interests : " + interestsList);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        List<User> allUsers = usersQueryService.getAll(interestsList, lookingForList, categoriesList);
        for (User u : allUsers) {
            JsonObjectBuilder jsonUserObjectBuilder = createFullJsonUser(u);
            jsonArrayBuilder.add(jsonUserObjectBuilder);
        }
        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

    @Override
    public Response getAllByLocation(@QueryParam("lon") Double lon, @QueryParam("lat") Double lat,
            @QueryParam("interests") String interests,
            @QueryParam("lookingFors") String lookingFors,
            @QueryParam("categories") String categories) throws ServiceException {
        List<String> interestsList = null;
        if (interests != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(interests.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            if (array != null) {
                interestsList = new ArrayList<String>(array.size());
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
        if (lookingFors != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(lookingFors.getBytes()));
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
        System.out.println("Looking Fors: " + lookingForList);
        System.out.println("I am : " + categoriesList);
        System.out.println("Interests : " + interestsList);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        List<User> usersInRange1 = usersQueryService.getUserByRange(lon, lat, 1.0, interestsList, lookingForList, categoriesList);
        for (User u : usersInRange1) {
            System.out.println("User in Range 1: " + u);
            JsonObjectBuilder jsonUserObjectBuilder = createFullJsonUser(u);
            jsonUserObjectBuilder.add("range", "1");
            jsonArrayBuilder.add(jsonUserObjectBuilder);
        }
        List<User> usersInRange2 = usersQueryService.getUserByRange(lon, lat, 3.0, interestsList, lookingForList, categoriesList);
        for (User u : usersInRange2) {
            if (!usersInRange1.contains(u)) {
                System.out.println("User in Range 2: " + u);
                JsonObjectBuilder jsonUserObjectBuilder = createFullJsonUser(u);
                jsonUserObjectBuilder.add("range", "2");
                jsonArrayBuilder.add(jsonUserObjectBuilder);
            }
        }
        List<User> usersInRange3 = usersQueryService.getUserByRange(lon, lat, 10.0, interestsList, lookingForList, categoriesList);
        for (User u : usersInRange3) {
            if (!usersInRange2.contains(u) && !usersInRange1.contains(u)) {
                System.out.println("User in Range 3: " + u);
                JsonObjectBuilder jsonUserObjectBuilder = createFullJsonUser(u);
                jsonUserObjectBuilder.add("range", "3");
                jsonArrayBuilder.add(jsonUserObjectBuilder);
            }
        }
        List<User> usersInRange4 = usersQueryService.getUserByRange(lon, lat, 50.0, interestsList, lookingForList, categoriesList);
        for (User u : usersInRange4) {
            if (!usersInRange3.contains(u) && !usersInRange2.contains(u) && !usersInRange1.contains(u)) {
                System.out.println("User in Range 4: " + u);
                JsonObjectBuilder jsonUserObjectBuilder = createFullJsonUser(u);
                jsonUserObjectBuilder.add("range", "4");
                jsonArrayBuilder.add(jsonUserObjectBuilder);
            }
        }
        List<User> usersInRange5 = usersQueryService.getUserByRange(lon, lat, 100.0, interestsList, lookingForList, categoriesList);
        for (User u : usersInRange5) {
            if (!usersInRange4.contains(u) && !usersInRange3.contains(u) && !usersInRange2.contains(u) && !usersInRange1.contains(u)) {
                System.out.println("User in Range 5: " + u);
                JsonObjectBuilder jsonUserObjectBuilder = createFullJsonUser(u);
                jsonUserObjectBuilder.add("range", "5");
                jsonArrayBuilder.add(jsonUserObjectBuilder);
            }
        }
        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

}
