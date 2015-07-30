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
    public Long newNotification(String nickname, String message, String action, String type) throws ServiceException {
        pushNotificaiton(nickname, message);
        return 0L;
    }

    @Override
    public List<Notification> getAllNotificationsByUser(String nickname) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void pushNotificaiton(String nickname, String message) throws ServiceException {
        try {
            System.out.println(">> Looking for Nickname in SessionMap: " + nickname);
            System.out.println(">> Session found: " + nickToSessionMap.get(nickname));
            Session session = nickToSessionMap.get(nickname);
            if (session != null) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException ex) {
            throw new ServiceException("Issue sending notification via ws remote session");
        }
    }

}
