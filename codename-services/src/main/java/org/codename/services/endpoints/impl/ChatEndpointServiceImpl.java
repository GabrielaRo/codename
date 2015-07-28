/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
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

    private final static Logger log = Logger.getLogger(ChatEndpointServiceImpl.class.getName());

    public static final ObjectMapper MAPPER = new ObjectMapper();


    public ChatEndpointServiceImpl() {

    }

    private Client getClient() {
        return ClientBuilder.newClient();
    }

    private String getChatServerUrl() {
        CHAT_SERVER_URL = System.getProperty("CHAT_SERVER_URL");
        if (CHAT_SERVER_URL == null || CHAT_SERVER_URL.equals("")) {
            CHAT_SERVER_URL = "http://localhost:5000/";
        }

        System.out.println("CHAT_SERVER_URL : " + CHAT_SERVER_URL);
        return CHAT_SERVER_URL;
    }

    @Override
    public Response sendMessage(Long userId, String conversationId, String message) throws ServiceException {
        String sendMessages = getChatServerUrl() + "messages";
        final MultivaluedMap<String, String> userData = new MultivaluedHashMap<String, String>();
        User byId = userService.getById(userId);

        userData.add("room", conversationId);
        userData.add("text", message);

        System.out.println(">> Chat Target (send message) : " + sendMessages);
        System.out.println(">> With TOken - > " + String.format("Bearer %s", byId.getChatToken()));
        Response response = getClient().target(sendMessages).request()
                .header(CodenameUtil.AUTH_HEADER_KEY, String.format("Bearer %s", byId.getChatToken())).post(Entity.form(userData));
        Map<String, Object> responseEntity = null;
        try {
            responseEntity = ServicesHelpers.getResponseEntity(response);
        } catch (JsonMappingException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Response entity for sending message: " + responseEntity);
        return Response.ok().build();
    }

    @Override
    public Response createConversation(Long userId, String other) throws ServiceException {
        String sendMessages = getChatServerUrl() + "rooms";
        final MultivaluedMap<String, String> userData = new MultivaluedHashMap<String, String>();
        User byId = userService.getById(userId);

        userData.add("name", byId.getNickname() + "_" + other);
        userData.add("slug", byId.getNickname() + "_" + other);
        userData.add("description", byId.getNickname() + " & " + other);

        System.out.println(">> Chat Target (create room) : " + sendMessages);
        System.out.println(">> With TOken - > " + String.format("Bearer %s", byId.getChatToken()));
        Response response = getClient().target(sendMessages).request()
                .header(CodenameUtil.AUTH_HEADER_KEY, String.format("Bearer %s", byId.getChatToken())).post(Entity.form(userData));
        Map<String, Object> responseEntity = null;
        try {
            responseEntity = ServicesHelpers.getResponseEntity(response);
        } catch (JsonMappingException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Response entity for creating room: " + responseEntity);
        System.out.println("Room ID: "+ responseEntity.get("id"));
        
        
        
        return Response.ok(responseEntity.get("id").toString()).build();
    }
    
    
    @Override
    public Response getRooms(Long userId) throws ServiceException {
        User byId = userService.getById(userId);
        String sendMessages = getChatServerUrl() + "rooms?users="+byId.getNickname();

        System.out.println(">> Chat Target (get rooms for user "+byId.getNickname()+") : " + sendMessages);
        Response response = getClient().target(sendMessages).request()
                .header(CodenameUtil.AUTH_HEADER_KEY, String.format("Bearer %s", byId.getChatToken())).get();

        List<Map<String, Object>> responseArray = null;
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        
        try {
            responseArray = ServicesHelpers.getResponseArray(response);

            for (Map<String, Object> room : responseArray) {
                if(room.get("name").toString().contains(byId.getNickname() + "_") 
                        || room.get("name").toString().contains("_"+byId.getNickname())){
                    System.out.println("My Room "+room);
                    JsonObjectBuilder jsonUserObjectBuilder = createJsonConversation(room);
                    jsonArrayBuilder.add(jsonUserObjectBuilder);
                }
            }
        } catch (JsonMappingException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }



    @Override
    public Response getMessages(Long userId, String conversationId) throws ServiceException {
        User byId = userService.getById(userId);
        String sendMessages = getChatServerUrl() + "rooms/" + conversationId + "/messages?expand=owner";

        System.out.println(">> Chat Target (get messages) : " + sendMessages);
        Response response = getClient().target(sendMessages).request()
                .header(CodenameUtil.AUTH_HEADER_KEY, String.format("Bearer %s", byId.getChatToken())).get();

        List<Map<String, Object>> responseArray = null;
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        try {
            responseArray = ServicesHelpers.getResponseArray(response);

            
            for (Map<String, Object> message : responseArray) {
                Map<String, Object> owner = (Map<String, Object>) message.get("owner");
                JsonObjectBuilder jsonUserObjectBuilder = createJsonMessage(message, owner);
                jsonArrayBuilder.add(jsonUserObjectBuilder);
            }
        } catch (JsonMappingException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

    @Override
    public Response getConnections(Long userId) throws ServiceException {
        User byId = userService.getById(userId);
        String getConnections = getChatServerUrl() + "/connections/user/" + byId.getNickname();

        System.out.println(">> Chat Target (get connections) : " + getConnections);
        Response response = getClient().target(getConnections).request()
                .header(CodenameUtil.AUTH_HEADER_KEY, String.format("Bearer %s", byId.getChatToken())).get();

        System.out.println(" >> Connections: " + response.readEntity(String.class));
        return Response.ok().build();
    }

    private JsonObjectBuilder createJsonMessage(Map<String, Object> message, Map<String, Object> owner) {

        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("id", (String) message.get("text"));
        jsonObjBuilder.add("text", (String) message.get("text"));
        jsonObjBuilder.add("owner_displayName", (String) owner.get("displayName"));
        jsonObjBuilder.add("owner_nickname", (String) owner.get("username"));
        jsonObjBuilder.add("time", (String) message.get("posted"));

        return jsonObjBuilder;
    }

    private JsonObjectBuilder createJsonConversation(Map<String, Object> room) {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("conversation_id", (String) room.get("id"));
        jsonObjBuilder.add("description", (String) room.get("description"));

        return jsonObjBuilder;
    }

}
