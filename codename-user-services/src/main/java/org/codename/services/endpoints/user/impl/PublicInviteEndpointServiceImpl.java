/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.user.impl;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.codename.core.user.api.InvitationsService;
import org.codename.core.exceptions.ServiceException;
import org.codename.services.endpoints.user.api.PublicInviteEndpointService;

/**
 *
 * @author grogdj
 */
@Stateless
public class PublicInviteEndpointServiceImpl implements PublicInviteEndpointService {

    @Inject
    private InvitationsService invitationService;

    private final static Logger log = Logger.getLogger(PublicInviteEndpointServiceImpl.class.getName());

    public PublicInviteEndpointServiceImpl() {

    }

    @Override
    public Response requestInvitation(String email) throws ServiceException {
        boolean ok = invitationService.requestInvitation(email);
        return Response.ok(ok).build();
    }

}
