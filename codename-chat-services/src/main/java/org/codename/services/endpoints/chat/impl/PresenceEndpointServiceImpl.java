/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.chat.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonReader;
import javax.ws.rs.core.Response;
import org.codename.core.exceptions.ServiceException;
import org.codename.services.endpoints.chat.api.PresenceEndpointService;
import org.codename.core.chat.api.PresenceService;

/**
 *
 * @author grogdj
 */
@Stateless
public class PresenceEndpointServiceImpl implements PresenceEndpointService {

    private final static Logger log = Logger.getLogger(PresenceEndpointServiceImpl.class.getName());

    @Inject
    private PresenceService presenceService;

    public PresenceEndpointServiceImpl() {

    }

    @Override
    public Response getAllNotificationsByUser(String nickname) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getUsersState(String users) throws ServiceException {
        List<String> nicknames = null;
        System.out.println("List of names: " + users);
        if (users != null) {
            JsonReader reader = Json.createReader(new ByteArrayInputStream(users.getBytes()));
            JsonArray array = reader.readArray();
            reader.close();

            if (array != null) {
                nicknames = new ArrayList<String>(array.size());
                for (int i = 0; i < array.size(); i++) {
                    log.info("nicknames[" + i + "]: " + array.getString(i));

                    nicknames.add(array.getString(i));
                }

            }
        }
        List< Boolean> usersState = presenceService.getUsersState(nicknames);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (Boolean b : usersState) {

            jsonArrayBuilder.add(b.toString());

        }

        return Response.ok(jsonArrayBuilder.build().toString()).build();

    }

}
