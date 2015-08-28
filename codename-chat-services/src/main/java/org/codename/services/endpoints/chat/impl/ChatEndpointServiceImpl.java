/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.chat.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import org.codename.core.chat.api.ChatService;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.chat.Message;
import org.codename.services.endpoints.chat.api.ChatEndpointService;
import org.codename.core.chat.api.PresenceService;
import org.codename.core.user.api.UsersService;
import org.codename.model.user.User;

/**
 *
 * @author grogdj
 */
@Stateless
public class ChatEndpointServiceImpl implements ChatEndpointService {

    private final static Logger log = Logger.getLogger(ChatEndpointServiceImpl.class.getName());

    @Inject
    private ChatService chatService;

    @Inject
    private PresenceService presenceService;

    @Inject
    private UsersService usersService;

    public ChatEndpointServiceImpl() {

    }

    @Override
    public Response sendMessage(String toUser, String sender, String message) throws ServiceException {
        chatService.sendMessage(toUser, sender, message);

        return Response.ok().build();
    }

    @Override
    public Response getUserInbox(String nickname) throws ServiceException {
        Map<String, List<Message>> inbox = chatService.getInbox(nickname);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (String user : inbox.keySet()) {
            JsonObjectBuilder jsonUserObjectBuilder = 
                    createJsonConversation(inbox.get(user).get(inbox.get(user).size() - 1), user);
            jsonArrayBuilder.add(jsonUserObjectBuilder);
        }
        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

    @Override
    public Response getConversationMessages(String nickname, String withUser) throws ServiceException {
        Map<String, List<Message>> inbox = chatService.getInbox(nickname);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        List<Message> messageWithUser = inbox.get(withUser);
        if (messageWithUser != null) {
            for (Message m : messageWithUser) {
                JsonObjectBuilder jsonUserObjectBuilder = createJsonMessage(m);
                jsonArrayBuilder.add(jsonUserObjectBuilder);
            }

        }
        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }


    private JsonObjectBuilder createJsonMessage(Message m) {
        User byId = usersService.getById(m.getSender());

        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("id", m.getId());
        jsonObjBuilder.add("text", m.getText());
        jsonObjBuilder.add("owner_nickname", byId.getNickname());
        jsonObjBuilder.add("description", byId.getFirstname() + " " + byId.getLastname());
        jsonObjBuilder.add("time", m.getTimestamp().getTime());

        return jsonObjBuilder;
    }

    private JsonObjectBuilder createJsonConversation(Message m, String user) throws ServiceException {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        User byNickName = usersService.getByNickName(user);

        jsonObjBuilder.add("other_nickname", user);
        
        jsonObjBuilder.add("description", byNickName.getFirstname() + " " + byNickName.getLastname());
        jsonObjBuilder.add("onlineStatus", ""+presenceService.isOnline(user));

        jsonObjBuilder.add("excerpt", m.getText());
        jsonObjBuilder.add("time", m.getTimestamp().getTime());
//        jsonObjBuilder.add("blocked", c.isBlocked());

        return jsonObjBuilder;
    }

}
