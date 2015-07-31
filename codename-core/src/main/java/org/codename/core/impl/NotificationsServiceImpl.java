/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import org.codename.core.api.NotificationsService;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.Notification;
import javax.websocket.Session;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class NotificationsServiceImpl implements NotificationsService {

    private Map<String, Session> nickToSessionMap = new HashMap<String, Session>();
    private Map<Session, String> sessionToNickMap = new HashMap<Session, String>();

    @Override
    public void addNewSession(final String nickname, Session client) {
        onlineStatusUpdate(nickname);
        nickToSessionMap.put(nickname, client);

    }

    @Override
    public void removeSession(Session client) throws ServiceException {
        String nickname = sessionToNickMap.get(client);
        if (nickname == null) {
            System.out.println(">>>>WARN: Removing session failed: session doesn't exist");
            return;
        }
        sessionToNickMap.remove(client);
        nickToSessionMap.remove(nickname);
    }

    @Override
    public void newNotification(String toNickname, String fromNickname, String message, String action, String type) throws ServiceException {
        try {
            System.out.println(">> Looking for Nickname in SessionMap: " + toNickname);
            System.out.println(">> Session found: " + nickToSessionMap.get(toNickname));
            Session session = nickToSessionMap.get(toNickname);
            JsonObjectBuilder jsonUserObjectBuilder = Json.createObjectBuilder();
            jsonUserObjectBuilder.add("type", "message");
            jsonUserObjectBuilder.add("from", fromNickname);
            jsonUserObjectBuilder.add("text", message);
            if (session != null) {
                session.getBasicRemote().sendText(jsonUserObjectBuilder.build().toString());
            }
        } catch (IOException ex) {
            System.out.println("ERROR: Issue sending notification via ws remote sessio");
            ex.printStackTrace();
        }
    }

    @Override
    public List<Notification> getAllNotificationsByUser(String nickname) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean isOnline(String nickname){
        System.out.println("Nicks available: "+nickToSessionMap.keySet());
        return nickToSessionMap.keySet().contains(nickname);
    }

    private void onlineStatusUpdate(String newUser) {
        JsonObjectBuilder jsonUserObjectBuilder = Json.createObjectBuilder();
        jsonUserObjectBuilder.add("type", "online");
        jsonUserObjectBuilder.add("online", "true");
        jsonUserObjectBuilder.add("user", newUser);
        for (String nick : nickToSessionMap.keySet()) {
            System.out.println("Sending new user: "+ newUser + "to client "+ nick);
            try {
                nickToSessionMap.get(nick).getBasicRemote().sendText(jsonUserObjectBuilder.build().toString());
            } catch (IOException ex) {
                System.out.println("ERROR: Issue sending online status update via ws remote session");
                ex.printStackTrace();
            }
        }
    }


}
