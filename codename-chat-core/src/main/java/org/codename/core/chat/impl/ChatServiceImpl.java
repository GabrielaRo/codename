/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.chat.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.codename.core.chat.api.ChatService;
import org.codename.core.user.api.UsersService;
import org.codename.core.exceptions.ServiceException;
import org.codename.core.util.PersistenceManager;
import org.codename.model.chat.Message;
import org.codename.core.chat.api.PresenceService;
import org.codename.model.user.User;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class ChatServiceImpl implements ChatService {

    @Inject
    private PersistenceManager pm;

    @Inject
    private UsersService usersService;

    @Inject
    private PresenceService presenceService;

    @Override
    public Long sendMessage(String toUser, String sender, String text) throws ServiceException {

        User senderUser = usersService.getByNickName(sender);
        User destinationUser = usersService.getByNickName(toUser);
        if (senderUser != null && destinationUser != null && true) {// Check if the user that I'm trying to send the message is my blocked list
            Message message = new Message(destinationUser.getId(), senderUser.getId(), text);
            pm.persist(message);
            presenceService.newNotification(toUser, sender, text, "message", "");
            return message.getId();
        } else {
            throw new ServiceException("You (" + sender + ") cannot message " + toUser + ", you have "
                    + "being blocked or the user doesn't exist anymore!");
        }
    }

    @Override
    public List<Message> getMessages(String nickname) throws ServiceException {
        User user = usersService.getByNickName(nickname);
        return pm.createNamedQuery("Messages.getByUser", Message.class).setParameter("user", user.getId()).getResultList();
    }

    @Override
    /*
     * This method creates the inbox for a user, but in a very inefficient way. 
     *  There is a lot of room for performance improvement here. 
    */
    public Map<String, List<Message>> getInbox(String nickname) throws ServiceException {
        User user = usersService.getByNickName(nickname);
        Map<String, List<Message>> inboxMap = new HashMap<String, List<Message>>();
        List<Message> messages = getMessages(nickname);
        for (Message m : messages) {
            if (m.getSender().equals(user.getId())) {
                User byId = usersService.getById(m.getToUser());
                if (inboxMap.get(byId.getNickname()) == null) {
                    inboxMap.put(byId.getNickname(), new ArrayList<Message>());
                }
                inboxMap.get(byId.getNickname()).add(m);
            } else if (m.getToUser().equals(user.getId())) {
                User byId = usersService.getById(m.getSender());
                if (inboxMap.get(byId.getNickname()) == null) {
                    inboxMap.put(byId.getNickname(), new ArrayList<Message>());
                }
                inboxMap.get(byId.getNickname()).add(m);
            }
        }
        return inboxMap;
    }

}
