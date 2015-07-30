/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.api;

import java.util.List;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.Notification;

/**
 *
 * @author salaboy
 */
public interface NotificationsService {

    void addNewSession(String token);

    void removeSession(String token) throws ServiceException;

    Long newNotification(Long userId, String message, String action, String type) throws ServiceException;

    List<Notification> getAllNotificationsByUser(Long userId);
}
