/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.api;

import java.util.List;

import org.codename.core.exceptions.ServiceException;
import org.codename.model.ContactMessage;

/**
 *
 * @author gabriela
 */
public interface ContactMessageService {

    public boolean sendContactMessage(String contactEmails, String contactName, String contactSubject, String contactMessageText, String contactMessageType) throws ServiceException;

    public List<ContactMessage> getAllMessages(String contactEmail) throws ServiceException;

    public List<ContactMessage> getUnrepliedMessages(boolean contactMessageReplied) throws ServiceException;

}
