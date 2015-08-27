/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.user.impl;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import org.codename.core.user.api.ContactMessageService;

import org.codename.core.exceptions.ServiceException;
import org.codename.model.user.ContactMessage;
import org.codename.services.endpoints.user.api.AdminContactMessageEndpointService;

/**
 *
 * @author grogdj
 */
@Stateless
public class AdminContactMessageEndpointServiceImpl implements AdminContactMessageEndpointService {

    @Inject
    private ContactMessageService contactMessageService;

    private final static Logger log = Logger.getLogger(AdminContactMessageEndpointServiceImpl.class.getName());

    public AdminContactMessageEndpointServiceImpl() {

    }

    @Override
    public Response getRepliedMessages(String replied) throws ServiceException {
        List<ContactMessage> allMessages = contactMessageService.getUnrepliedMessages(Boolean.getBoolean(replied));
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (ContactMessage m : allMessages) {
            JsonObjectBuilder jsonUserObjectBuilder = createJsonContactMessage(m);
            jsonArrayBuilder.add(jsonUserObjectBuilder);
        }
        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }

    @Override
    public Response getAll(String email) throws ServiceException {
        List<ContactMessage> allMessages = contactMessageService.getAllMessages(email);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (ContactMessage m : allMessages) {
            JsonObjectBuilder jsonUserObjectBuilder = createJsonContactMessage(m);
            jsonArrayBuilder.add(jsonUserObjectBuilder);
        }
        return Response.ok(jsonArrayBuilder.build().toString()).build();
    }


    public static JsonObjectBuilder createJsonContactMessage(ContactMessage m) {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("email", (m.getContactEmail() == null) ? "" : m.getContactEmail());
        jsonObjBuilder.add("subject", (m.getContactSubject() == null) ? "" : m.getContactSubject());
        jsonObjBuilder.add("name", (m.getContactName() == null) ? "" : m.getContactName());
        jsonObjBuilder.add("text", (m.getContactMessageText() == null) ? "" : m.getContactMessageText());
        jsonObjBuilder.add("replied", (m.getContactMessageReplied() == null) ? "" : m.getContactMessageReplied().toString());
        jsonObjBuilder.add("date", (m.getContactMessageDate() == null) ? "" : "" + m.getContactMessageDate().getTime());
        jsonObjBuilder.add("date", (m.getContactMessageType() == null) ? "" : "" + m.getContactMessageType());

        return jsonObjBuilder;
    }

}
