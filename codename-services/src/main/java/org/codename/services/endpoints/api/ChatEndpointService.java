/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.api;

import java.io.Serializable;
import javax.ejb.Local;
import javax.validation.constraints.NotNull;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codename.core.exceptions.ServiceException;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author grogdj
 */
@Local
@Path("/chat")
public interface ChatEndpointService extends Serializable {

    @POST
    @Path("/conversations/{conversationId}/message")
    @Produces({MediaType.APPLICATION_JSON})
    public Response sendMessage(@NotNull @PathParam("conversationId") Long conversationId,
            @NotNull @NotEmpty @FormParam("sender") String sender,
            @NotNull @NotEmpty @FormParam("message") String message) throws ServiceException;

    @GET
    @Path("/conversations/{conversationId}/messages")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMessages(@NotNull @PathParam("conversationId") Long conversationId) throws ServiceException;

    @POST
    @Path("/conversations/create")
    @Produces({MediaType.APPLICATION_JSON})
    public Response createConversation(@NotNull @NotEmpty @FormParam("initiator") String initiator, @NotNull @NotEmpty @FormParam("other") String other) throws ServiceException;

    @GET
    @Path("/conversations/{user}")
    @Produces({MediaType.APPLICATION_JSON})
    Response getConversations(@NotNull @NotEmpty @PathParam("user") String user) throws ServiceException;

}
