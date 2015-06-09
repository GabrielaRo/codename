/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.api;

import org.codename.model.Notification;
import java.util.List;
import javax.websocket.Session;
import org.codename.services.exceptions.ServiceException;

/**
 *
 * @author grogdj
 */
public interface NotificationsService {

   void addNewSession(String token, Session session); 
   
   void removeSession(Session session) throws ServiceException;
    
   Long newNotification(Long userId, String message, String action, String type) throws ServiceException;
   
   List<Notification> getAllNotificationsByUser(Long userId);
   
}
