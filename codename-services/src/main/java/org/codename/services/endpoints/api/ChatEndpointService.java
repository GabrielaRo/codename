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
import javax.ws.rs.QueryParam;
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
    @Path("/messages")
    @Produces({MediaType.APPLICATION_JSON})
    public Response sendMessage(@NotNull @FormParam("userId") Long userId, @NotNull @NotEmpty @FormParam("conversationId") String conversationId,  @NotNull @NotEmpty @FormParam("message") String message) throws ServiceException;
    
    @GET
    @Path("/messages/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMessages(@NotNull @PathParam("userId") Long userId, @QueryParam("conversationId") String conversationId) throws ServiceException;
    
//    @GET
//    @Path("/conversations/{userId}")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response getConversations(@NotNull @PathParam("userId") Long userId) throws ServiceException;
    
    @POST
    @Path("/conversations/{userId}/create")
    @Produces({MediaType.APPLICATION_JSON})
    public Response createConversation(@NotNull @PathParam("userId") Long userId, @FormParam("other") String other) throws ServiceException;

    @GET
    @Path("/connections/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getConnections(@NotNull @PathParam("userId") Long userId) throws ServiceException;
    
    @GET
    @Path("/conversations/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    Response getRooms(@NotNull @PathParam("userId")Long userId) throws ServiceException ;
   
}
