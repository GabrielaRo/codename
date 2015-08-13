/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.impl;

import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.codename.core.api.ContactMessageService;
import org.codename.core.api.InvitationsService;
import org.codename.core.exceptions.ServiceException;
import org.codename.core.util.PersistenceManager;
import org.codename.model.ContactMessage;
import org.codename.model.Invitation;
import org.codename.model.Message;

/**
 *
 * @author gabriela
 */
@ApplicationScoped
public class ContactMessageServiceImpl implements ContactMessageService {

    @Inject
    private PersistenceManager pm;

    private final static Logger log = Logger.getLogger(ContactMessageServiceImpl.class.getName());

    public ContactMessageServiceImpl() {
    }

	@Override
	public boolean sendContactMessage(String contactEmail, String contactName, String contactSubject, String contactMessageText, String contactMessageType) throws ServiceException {
		ContactMessage contactMessage = null;
		try {
			contactMessage = new ContactMessage(contactEmail, contactName, contactSubject, contactMessageText, contactMessageType);
		} catch (NoResultException nre) {
			
		}
		if (contactMessage != null) {
			return false;
		}
		contactMessage = new ContactMessage(contactEmail, contactName, contactSubject, contactMessageText, contactMessageType);
		pm.persist(contactMessage);
		return true;
	}
	
	@Override
	public List<ContactMessage> getAllMessages(String contactEmail) throws ServiceException {
		return pm.createNamedQuery("ContactMessage.getContactMessage", ContactMessage.class).setParameter("contactEmail", contactEmail).getResultList();
	}
	
	@Override
	public List<ContactMessage> getUnrepliedMessages(boolean contactMessageReplied) throws ServiceException {
		return pm.createNamedQuery("ContactMessage.getContactMessage", ContactMessage.class).setParameter("contactMessageReplied", contactMessageReplied).getResultList();
	}
}
