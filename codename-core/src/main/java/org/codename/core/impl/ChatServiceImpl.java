/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.impl;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.codename.core.api.ChatService;
import org.codename.core.exceptions.ServiceException;
import org.codename.core.util.PersistenceManager;
import org.codename.model.Conversation;
import org.codename.model.Message;

/**
 *
 * @author salaboy
 */
@ApplicationScoped
public class ChatServiceImpl implements ChatService {

    @Inject
    private PersistenceManager pm;

    @Override
    public Long sendMessage(Long conversationId, String sender, String text) throws ServiceException {
        Conversation conv = pm.find(Conversation.class, conversationId);
        if (conv != null && !conv.isBlocked()) {
            Message message = new Message(conversationId, sender, text);
            pm.persist(message);
            return message.getId();
        } else {
            throw new ServiceException("Conversation not found or conversation blocked");
        }
    }

    @Override
    public List<Message> getMessages(Long conversationId) throws ServiceException {
        return pm.createNamedQuery("Messages.getConversation", Message.class).setParameter("conversationId", conversationId).getResultList();
    }

    @Override
    public List<Conversation> getConversations(String userId) throws ServiceException {
        return pm.createNamedQuery("Conversations.byUser", Conversation.class).setParameter("userId", userId).getResultList();
    }

    @Override
    public Long createConversation(String userA, String userB) throws ServiceException {
        try {
            Conversation conv = pm.createNamedQuery("Conversations.byParticipants", Conversation.class)
                    .setParameter("participantA", userA)
                    .setParameter("participantB", userB).getSingleResult();

            return conv.getId();

        } catch (NoResultException nre) {
            // Do nothing just create the conversation            
        }
        Conversation conversation = new Conversation(userA, userB);
        pm.persist(conversation);
        return conversation.getId();
    }

    @Override
    public boolean removeConversation(String userA, String userB) throws ServiceException {
        try {
            Conversation conv = pm.createNamedQuery("Conversations.byParticipants", Conversation.class)
                    .setParameter("participantA", userA)
                    .setParameter("participantB", userB).getSingleResult();

            pm.remove(conv);
            return true;
        } catch (NoResultException nre) {
            //throw new ServiceException("No conversation found for " + userA + " and " + userB);
            return false;
        }
    }

    @Override
    public boolean removeConversation(Long conversationId) throws ServiceException {
        try {
            Conversation conv = pm.find(Conversation.class, conversationId);
            pm.remove(conv);
            return true;
        } catch (NoResultException nre) {
            //throw new ServiceException("No conversation found for " + userA + " and " + userB);
            return false;
        }
    }

    @Override
    public boolean blockConversation(String userA, String userB) throws ServiceException {
        try {
            Conversation conv = pm.createNamedQuery("Conversations.byParticipants", Conversation.class)
                    .setParameter("participantA", userA)
                    .setParameter("participantB", userB).getSingleResult();
            conv.setBlocked(true);
            pm.merge(conv);
            return true;
        } catch (NoResultException nre) {
            //throw new ServiceException("No conversation found for " + userA + " and " + userB);
            return false;
        }
    }

    @Override
    public boolean blockConversation(Long conversationId) throws ServiceException {
        try {
            Conversation conv = pm.find(Conversation.class, conversationId);
            conv.setBlocked(true);
            pm.merge(conv);
            return true;
        } catch (NoResultException nre) {
            //throw new ServiceException("No conversation found for " + userA + " and " + userB);
            return false;
        }
    }

}
