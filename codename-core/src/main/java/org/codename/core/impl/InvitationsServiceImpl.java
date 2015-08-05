/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.core.impl;

import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.codename.core.api.InvitationsService;
import org.codename.core.exceptions.ServiceException;
import org.codename.core.util.PersistenceManager;
import org.codename.model.Invitation;

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
        Invitation invitation = pm.createNamedQuery("Invitations.getByEmail", Invitation.class).getSingleResult();
        if(invitation != null){
            return false;
        }
        invitation = new Invitation(email);
        pm.persist(invitation);
        return true;
    }

    

}
