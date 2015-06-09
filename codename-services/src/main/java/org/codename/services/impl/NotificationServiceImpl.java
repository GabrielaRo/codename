/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.websocket.Session;
import org.codename.model.Notification;
import org.codename.model.User;
import org.codename.services.api.NotificationsService;
import org.codename.services.exceptions.ServiceException;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class NotificationServiceImpl implements NotificationsService {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    private final static Logger log = Logger.getLogger(NotificationServiceImpl.class.getName());

    private Map<String, Session> emailToSessionMap = new HashMap<String, Session>();
    private Map<Session, String> sessionToEmailMap = new HashMap<Session, String>();
    
    @Override
    public Long newNotification(Long userId, String message, String action, String type) throws ServiceException {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new ServiceException("User  doesn't exist: " + userId);
        }
        
        Notification notification = new Notification(user, message, action, Notification.NotificationType.valueOf(type));
        em.persist(notification);
        log.log(Level.INFO, "Notification {0} created with id {1}", new Object[]{message, notification.getId()});
        pushNotificaiton(user.getEmail(), notification.getId().toString());
        return notification.getId();
    }

    @Override
    public List<Notification> getAllNotificationsByUser(Long userId) {
        return em.createNamedQuery("Notification.getAllByUser", Notification.class).setParameter("userId", userId).getResultList();
    }

    public void addNewSession(String email, Session session) {
        System.out.println(">>>> Adding new session to email: "+email);
        emailToSessionMap.put(email, session);
        sessionToEmailMap.put(session, email);
    }

    public void removeSession(Session session) throws ServiceException{
        String email = sessionToEmailMap.get(session);
        if(email == null){
            throw new ServiceException("Removing session failed: session doesn't exist");
        }
        sessionToEmailMap.remove(session);
        emailToSessionMap.remove(email);
    }
    
    
    
    private void pushNotificaiton(String email, String id) {
        try {
            System.out.println(">> Looking for Email in SessionMap: "+email);
            System.out.println(">> Session found: "+emailToSessionMap.get(email));
            Session session = emailToSessionMap.get(email);
            if(session != null){
                session.getBasicRemote().sendText(id);
            }
        } catch (IOException ex) {
            Logger.getLogger(NotificationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
