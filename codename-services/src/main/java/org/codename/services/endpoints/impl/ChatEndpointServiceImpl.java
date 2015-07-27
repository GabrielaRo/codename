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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private static final String ROOM = "55b6751d951f7a5d020f58eb";

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
    public Response sendMessage(Long userId, String to, String message) throws ServiceException {
        String sendMessages = getChatServerUrl() + "messages";
        final MultivaluedMap<String, String> userData = new MultivaluedHashMap<String, String>();
        User byId = userService.getById(userId);

        userData.add("room", ROOM);
        userData.add("text", "@" + to + " " + message);

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
    public Response getConversations(Long userId) throws ServiceException {
        User byId = userService.getById(userId);
        String sendMessages = getChatServerUrl() + "rooms/" + ROOM + "/messages?expand=owner";

        System.out.println(">> Chat Target (get messages) : " + sendMessages);
        Response response = getClient().target(sendMessages).request()
                .header(CodenameUtil.AUTH_HEADER_KEY, String.format("Bearer %s", byId.getChatToken())).get();

//        Map<String, Object> responseEntity = null;
//        try {
//            responseEntity = ServicesHelpers.getResponseEntity(response);
//        } catch (JsonMappingException ex) {
//            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println(">>(Get Messages) Response entity for getting message: "+ responseEntity);
        //System.out.println(" >> Messages: "+ response.readEntity(String.class));
        List<Map<String, Object>> responseArray = null;
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        Map<String, JsonObjectBuilder> conversationParticipants = new HashMap<String, JsonObjectBuilder>();
        try {
            responseArray = ServicesHelpers.getResponseArray(response);
//            System.out.println(" >> Messages array size = " + responseArray.size());
//            System.out.println(" >> Messages array = " + responseArray);
            
            
            for (Map<String, Object> message : responseArray) {
                Map<String, Object> owner = (Map<String, Object>) message.get("owner");
                String ownerId = (String) owner.get("id");
                String text = (String) message.get("text");
                JsonObjectBuilder jsonUserObjectBuilder = createJsonConversation(message, owner);

//                System.out.println("Message Owner ("+owner.get("username")+") - Text: "+text);
                if (text.contains(byId.getNickname())) {
                    System.out.println("Message to me from (" + owner.get("username") + ") - Text: " + text);
                    conversationParticipants.put(ownerId, jsonUserObjectBuilder);
                } else {
                    System.out.println("Not adding this meesage: " + message);
                }
            }
        } catch (JsonMappingException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(String participant : conversationParticipants.keySet()){
            jsonArrayBuilder.add(conversationParticipants.get(participant));
        }

        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

    @Override
    public Response getMessages(Long userId, String other) throws ServiceException {
        User byId = userService.getById(userId);
        String sendMessages = getChatServerUrl() + "rooms/" + ROOM + "/messages?expand=owner";

        System.out.println(">> Chat Target (get messages) : " + sendMessages);
        Response response = getClient().target(sendMessages).request()
                .header(CodenameUtil.AUTH_HEADER_KEY, String.format("Bearer %s", byId.getChatToken())).get();

//        Map<String, Object> responseEntity = null;
//        try {
//            responseEntity = ServicesHelpers.getResponseEntity(response);
//        } catch (JsonMappingException ex) {
//            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println(">>(Get Messages) Response entity for getting message: "+ responseEntity);
        //System.out.println(" >> Messages: "+ response.readEntity(String.class));
        List<Map<String, Object>> responseArray = null;
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        try {
            responseArray = ServicesHelpers.getResponseArray(response);
//            System.out.println(" >> Messages array size = " + responseArray.size());
//            System.out.println(" >> Messages array = " + responseArray);
            System.out.println("Fitering by users: " + byId.getNickname() + " and " + other);
            for (Map<String, Object> message : responseArray) {
                Map<String, Object> owner = (Map<String, Object>) message.get("owner");
                String text = (String) message.get("text");
                JsonObjectBuilder jsonUserObjectBuilder = createJsonMessage(message, owner);

//                System.out.println("Message Owner ("+owner.get("username")+") - Text: "+text);
                if (owner.get("username").equals(other) && text.contains(byId.getNickname())) {
                    System.out.println("Message to me from (" + owner.get("username") + ") - Text: " + text);
                    jsonArrayBuilder.add(jsonUserObjectBuilder);
                } else if (owner.get("username").equals(byId.getNickname()) && text.contains(other)) {
                    System.out.println("Message from me to (" + other + ") - Text: " + text);
                    jsonArrayBuilder.add(jsonUserObjectBuilder);
                } else {
                    System.out.println("Not adding this meesage: " + message);
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
    public Response getConnections(Long userId) throws ServiceException {
        User byId = userService.getById(userId);
        String getConnections = getChatServerUrl() + "/connections/user/" + byId.getNickname();

        System.out.println(">> Chat Target (get connections) : " + getConnections);
        Response response = getClient().target(getConnections).request()
                .header(CodenameUtil.AUTH_HEADER_KEY, String.format("Bearer %s", byId.getChatToken())).get();
//        Map<String, Object> responseEntity = null;
//        try {
//            responseEntity = ServicesHelpers.getResponseEntity(response);
//        } catch (JsonMappingException ex) {
//            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ChatEndpointServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println(">> (getPrecense)Response entity for getting message: "+ responseEntity);
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

    private JsonObjectBuilder createJsonConversation(Map<String, Object> message, Map<String, Object> owner) {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("participant_displayName", (String) owner.get("displayName"));
        jsonObjBuilder.add("participant_nickname", (String) owner.get("username"));
        jsonObjBuilder.add("excerpt", (String) message.get("text"));
        jsonObjBuilder.add("time", (String) message.get("posted"));

        return jsonObjBuilder;
    }

}
