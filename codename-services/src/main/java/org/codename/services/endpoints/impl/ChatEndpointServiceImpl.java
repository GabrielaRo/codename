/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.codename.core.api.UsersService;
import org.codename.core.exceptions.ServiceException;
import org.codename.core.util.CodenameUtil;
import org.codename.model.User;
import org.codename.services.endpoints.api.ChatEndpointService;
import org.codename.services.util.ServicesHelpers;

/**
 *
 * @author grogdj
 */
@Stateless
public class ChatEndpointServiceImpl implements ChatEndpointService {

    @Inject
    private UsersService userService;

    private String CHAT_SERVER_URL;

    private String CHAT_SERVER_ENABLED;

    private final static Logger log = Logger.getLogger(ChatEndpointServiceImpl.class.getName());

    public static final ObjectMapper MAPPER = new ObjectMapper();
    
    private static final String ROOM = "55acf2d18cc64e9492e4dfb1";

    public ChatEndpointServiceImpl() {

    }

    private Client getClient() {
        return ClientBuilder.newClient();
    }

    private boolean isChatServerEnabled() {
        CHAT_SERVER_ENABLED = System.getProperty("CHAT_SERVER_ENABLED");
        System.out.println("CHAT_SERVER_ENABLED : " + CHAT_SERVER_ENABLED);
        if (CHAT_SERVER_ENABLED != null && !CHAT_SERVER_ENABLED.equals("")) {
            return Boolean.parseBoolean(CHAT_SERVER_ENABLED);
        }

        return true;
    }

    private String getChatServerUrl() {
        CHAT_SERVER_URL = System.getProperty("CHAT_SERVER_URL");
        if (CHAT_SERVER_URL == null || CHAT_SERVER_URL.equals("")) {
            CHAT_SERVER_URL = "http://localhost:5000/";
        }

        System.out.println("CHAT_SERVER_URL : " + CHAT_SERVER_URL);
        return CHAT_SERVER_URL;
    }

    public Response sendMessage(Long userId, String to, String message) throws ServiceException {
        String sendMessages = getChatServerUrl() + "messages";
        final MultivaluedMap<String, String> userData = new MultivaluedHashMap<String, String>();
        User byId = userService.getById(userId);
        
        userData.add("room", ROOM);
        userData.add("text", "@"+to + " " + message);

        System.out.println(">> Chat Target (send message) : " + sendMessages);
        System.out.println(">> With TOken - > "+ String.format("Bearer %s", byId.getChatToken()));
        Response response = getClient().target(sendMessages).request().header(CodenameUtil.AUTH_HEADER_KEY, String.format("Bearer %s", byId.getChatToken())).post(Entity.form(userData));
        Map<String, Object> responseEntity = null;
        try {
            responseEntity = ServicesHelpers.getResponseEntity(response);
        } catch (JsonMappingException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Response entity for sending message: "+ responseEntity);
        return Response.ok().build();
    }

    public Response getMessages(Long userId) throws ServiceException {
        String sendMessages = getChatServerUrl() + "rooms/"+ROOM+"/messages";
        final MultivaluedMap<String, String> userData = new MultivaluedHashMap<String, String>();

        

        System.out.println(">> Chat Target (send message) : " + sendMessages);
        Response response = getClient().target(sendMessages).request().post(Entity.form(userData));
        Map<String, Object> responseEntity = null;
        try {
            responseEntity = ServicesHelpers.getResponseEntity(response);
        } catch (JsonMappingException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Response entity for getting message: "+ responseEntity);
        
        return Response.ok().build();
    }

}
