/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.chat.api;

import java.util.List;
import java.util.Map;
import org.codename.core.exceptions.ServiceException;
import org.codename.model.chat.Message;

/**
 *
 * @author grogdj
 */
public interface ChatService {

    public Long sendMessage(String toUser, String sender, String text) throws ServiceException;;

    public List<Message> getMessages(String user) throws ServiceException;
    
    public Map<String, List<Message>> getInbox(String nickname) throws ServiceException;

    public List<Message> getMessagesIndex(String nickname, Integer offset, Integer limit) throws ServiceException;

}
