/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.impl;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.codename.core.api.ContactMessageService;

import org.codename.core.exceptions.ServiceException;
import org.codename.services.endpoints.api.PublicContactMessageEndpointService;

/**
 *
 * @author grogdj
 */
@Stateless
public class PublicContactMessageEndpointServiceImpl implements PublicContactMessageEndpointService {

    @Inject
    private ContactMessageService contactMessageService;

    private final static Logger log = Logger.getLogger(PublicContactMessageEndpointServiceImpl.class.getName());

    public PublicContactMessageEndpointServiceImpl() {

    }

    @Override
    public Response sendContactMessage(String email, String name, String subject, String text, String type) throws ServiceException {
        System.out.println("Recieving request to send a message : " + email + name + subject + text + type);
        contactMessageService.sendContactMessage(email, name, subject, text, type);
        return Response.ok().build();
    }

}
