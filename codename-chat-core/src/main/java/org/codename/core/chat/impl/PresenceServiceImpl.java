/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.chat.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.chat.Notification;
import javax.websocket.Session;
import org.codename.core.chat.api.ChatService;
import org.codename.core.chat.api.PresenceService;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class PresenceServiceImpl implements PresenceService {

    private Map<String, Session> nickToSessionMap = new HashMap<String, Session>();
    private Map<Session, String> sessionToNickMap = new HashMap<Session, String>();

    @Inject
    private ChatService chatService;

    @Override
    public void userJoin(final String nickname, Session client) throws ServiceException {
        statusUpdate(nickname, true);
        nickToSessionMap.put(nickname, client);
        sessionToNickMap.put(client, nickname);
    }

    @Override
    public void userLeave(Session client) throws ServiceException {
        String nickname = sessionToNickMap.get(client);
        if (nickname == null) {
            return;
        }
        statusUpdate(nickname, false);
        sessionToNickMap.remove(client);
        nickToSessionMap.remove(nickname);
    }

    @Override
    public void newNotification(String toNickname, String fromNickname, String message, String action, String type) throws ServiceException {
        try {
            Session session = nickToSessionMap.get(toNickname);
            JsonObjectBuilder jsonUserObjectBuilder = Json.createObjectBuilder();
            jsonUserObjectBuilder.add("type", "message");
            jsonUserObjectBuilder.add("from", fromNickname);
            jsonUserObjectBuilder.add("to", toNickname);
            jsonUserObjectBuilder.add("text", message);
            jsonUserObjectBuilder.add("description", type);

            if (session != null) {
                session.getBasicRemote().sendText(jsonUserObjectBuilder.build().toString());
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    @Override
    public List<Notification> getAllNotificationsByUser(String nickname) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isOnline(String nickname) {
        return nickToSessionMap.keySet().contains(nickname);
    }

    @Override
    public List<Boolean> getUsersState(List<String> nicknames) {
        List<Boolean> precenses = new ArrayList<Boolean>();
        for (String n : nicknames) {
            precenses.add(nickToSessionMap.keySet().contains(n));
        }
        return precenses;
    }

    private void statusUpdate(String userNickname, boolean online) throws ServiceException {

//        List<Conversation> conversations = chatService.getConversations(userNickname);
//
//        if (!conversations.isEmpty()) {
//
//            JsonObjectBuilder jsonUserObjectBuilder = Json.createObjectBuilder();
//            if (online) {
//                jsonUserObjectBuilder.add("type", "online");
//            } else {
//                jsonUserObjectBuilder.add("type", "offline");
//            }
//            jsonUserObjectBuilder.add("online", online);
//            jsonUserObjectBuilder.add("user", userNickname);
//            for (Conversation c : conversations) {
//                String sendTo = (!c.getUserA().equals(userNickname)) ? c.getUserA() : c.getUserB();
//                if (nickToSessionMap.keySet().contains(sendTo)) {
//                    try {
//                        jsonUserObjectBuilder.add("conversationId", c.getId());
//                        nickToSessionMap.get(sendTo).getBasicRemote().sendText(jsonUserObjectBuilder.build().toString());
//                    } catch (IOException ex) {
//
//                        ex.printStackTrace();
//                    }
//
//                }
//
//            }
//        }
    }

}
