/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.impl;

import java.util.List;
import org.codename.core.api.NotificationsService;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.Notification;

/**
 *
 * @author salaboy
 */
public class NotificationsServiceImpl implements NotificationsService {

    @Override
    public void addNewSession(final String token) {
       

    }

    @Override
    public void removeSession(String token) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    @Override
    public Long newNotification(Long userId, String message, String action, String type) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Notification> getAllNotificationsByUser(Long userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
