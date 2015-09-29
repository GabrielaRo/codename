/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.tag.api;

import java.io.Serializable;
import javax.ejb.Local;
import javax.validation.constraints.NotNull;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
@Path("public/tags")
public interface TagEndpointService extends Serializable {

//    @POST
//    @Path("/message")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response sendMessage(@NotNull @FormParam("toUser") String toUser,
//            @NotNull @NotEmpty @FormParam("sender") String sender,
//            @NotNull @NotEmpty @FormParam("message") String message) throws ServiceException;
//
//    @GET
//    @Path("/inbox")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response getUserInbox(@NotNull @QueryParam("nickname") String nickname) throws ServiceException;

    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    Response getAllTags() throws ServiceException;

}
