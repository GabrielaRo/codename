/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.user.impl;

import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.codename.core.user.api.InvitationsService;
import org.codename.core.exceptions.ServiceException;
import org.codename.core.util.PersistenceManager;
import org.codename.model.user.Invitation;

/**
 *
 * @author grogdj
 */
@ApplicationScoped
public class InvitationsServiceImpl implements InvitationsService {

    @Inject
    private PersistenceManager pm;

    private final static Logger log = Logger.getLogger(InvitationsServiceImpl.class.getName());

    public InvitationsServiceImpl() {
    }

    @Override
    public boolean requestInvitation(String email) throws ServiceException {
        
        Invitation invitation = null;
        try{
            invitation = pm.createNamedQuery("Invitations.getByEmail", Invitation.class).setParameter("email", email).getSingleResult();
        } catch(NoResultException nre){
        
        }
        if(invitation != null){
            return false;
        }
        invitation = new Invitation(email);
        pm.persist(invitation);
        return true;
    }

    

}
