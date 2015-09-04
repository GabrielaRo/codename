/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.tracking.impl;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.codename.core.tracking.api.ShareLocationService;
import org.codename.core.util.PersistenceManager;
import org.codename.model.tracking.SharedLocation;
import org.hibernate.service.spi.ServiceException;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class ShareLocationServiceImpl implements ShareLocationService {

    @Inject
    private PersistenceManager pm;
    
    

    
//    @Override
//    public Long sendMessage(String toUser, String sender, String text) throws ServiceException {
//
//        User senderUser = usersService.getByNickName(sender);
//        User destinationUser = usersService.getByNickName(toUser);
//        if (senderUser != null && destinationUser != null && true) {// Check if the user that I'm trying to send the message is my blocked list
//            Message message = new Message(destinationUser.getId(), senderUser.getId(), text);
//            pm.persist(message);
//            presenceService.newNotification(toUser, sender, text, "message", senderUser.getFirstname() +" "+ senderUser.getLastname());
//            return message.getId();
//        } else {
//            throw new ServiceException("You (" + sender + ") cannot message " + toUser + ", you have "
//                    + "being blocked or the user doesn't exist anymore!");
//        }
//    }
//
//    @Override
//    public List<Message> getMessages(String nickname) throws ServiceException {
//        
//        User user = usersService.getByNickName(nickname);
//        
//        return pm.createNamedQuery("Messages.getByUser", Message.class)
//                .setParameter("user", user.getId())
////                .setFirstResult(offset)
////                .setMaxResults(limit)
//                .getResultList();
//    }

    @Override
    public void shareLocation(Long userId, Double latitude, Double longitude) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SharedLocation> getSharedLocations(Long userId) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

}
