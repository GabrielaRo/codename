/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.api;

import java.util.List;
import javax.websocket.Session;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.Notification;

/**
 *
 * @author grogdj
 */
public interface NotificationsService {

    void addNewSession(String nickname, Session client) throws ServiceException;

    void removeSession(Session client) throws ServiceException;

    void newNotification(String toNickname, String fromNickname, String message, String action, String type) throws ServiceException;

    List<Notification> getAllNotificationsByUser(String nickname);
    
    boolean isOnline(String nickname);
}
