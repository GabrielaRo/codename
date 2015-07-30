/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.api;

import java.util.List;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.Conversation;
import org.codename.model.Message;

/**
 *
 * @author grogdj
 */
public interface ChatService {

    public Long sendMessage(Long conversationId, String sender, String text) throws ServiceException;;

    public List<Message> getMessages(Long conversationId) throws ServiceException;

    public List<Conversation> getConversations(String userId) throws ServiceException;

    public Long createConversation(String userA, String userB) throws ServiceException;
    
    public boolean removeConversation(String userA, String userB) throws ServiceException;
    
    public boolean removeConversation(Long conversationId) throws ServiceException;
    
    public boolean blockConversation(String userA, String userB) throws ServiceException;
    
    public boolean blockConversation(Long conversationId) throws ServiceException;

}
