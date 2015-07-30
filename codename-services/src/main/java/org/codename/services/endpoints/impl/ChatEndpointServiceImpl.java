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
import org.codename.core.api.ChatService;
import org.codename.core.api.NotificationsService;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.Conversation;
import org.codename.model.Message;
import org.codename.services.endpoints.api.ChatEndpointService;

/**
 *
 * @author grogdj
 */
@Stateless
public class ChatEndpointServiceImpl implements ChatEndpointService {

    private final static Logger log = Logger.getLogger(ChatEndpointServiceImpl.class.getName());

    @Inject
    private ChatService chatService;
    

    public ChatEndpointServiceImpl() {

    }
    
    @Override
    public Response sendMessage(Long conversationId, String sender, String message) throws ServiceException {
        chatService.sendMessage(conversationId, sender, message);
        
        return Response.ok().build();
    }

    @Override
    public Response blockConversation(Long conversationId) throws ServiceException {
        chatService.blockConversation(conversationId);
        return Response.ok().build();
    }
    
    @Override
    public Response unblockConversation(Long conversationId) throws ServiceException {
        chatService.unblockConversation(conversationId);
        return Response.ok().build();
    }

    @Override
    public Response createConversation(String initiator, String otherFhellow) throws ServiceException {
        Long conversationId = chatService.createConversation(initiator, otherFhellow);

        return Response.ok(conversationId).build();
    }

    @Override
    public Response getConversations(String user) throws ServiceException {
        List<Conversation> conversations = chatService.getConversations(user);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (Conversation c : conversations) {

            JsonObjectBuilder jsonUserObjectBuilder = createJsonConversation(c, user);
            jsonArrayBuilder.add(jsonUserObjectBuilder);

        }

        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

    @Override
    public Response getMessages(Long conversationId) throws ServiceException {
        List<Message> messages = chatService.getMessages(conversationId);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (Message m : messages) {
            JsonObjectBuilder jsonUserObjectBuilder = createJsonMessage(m);
            jsonArrayBuilder.add(jsonUserObjectBuilder);
        }

        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

    private JsonObjectBuilder createJsonMessage(Message m) {

        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("id", m.getId());
        jsonObjBuilder.add("text", m.getText());
        jsonObjBuilder.add("owner_nickname", m.getSender());
        jsonObjBuilder.add("time", m.getTimestamp().getTime());

        return jsonObjBuilder;
    }

    private JsonObjectBuilder createJsonConversation(Conversation c, String user) {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("conversation_id", c.getId());
        if(c.getUserA().equals(user)){
            jsonObjBuilder.add("other_nickname", c.getUserB());
            jsonObjBuilder.add("description", c.getUserBFullName());
        }else{
            jsonObjBuilder.add("other_nickname", c.getUserA());
            jsonObjBuilder.add("description", c.getUserAFullName());
        }
        jsonObjBuilder.add("excerpt", c.getExcerpt());
        jsonObjBuilder.add("time", c.getTimestamp().getTime());
        jsonObjBuilder.add("blocked", c.isBlocked());

        return jsonObjBuilder;
    }

}
