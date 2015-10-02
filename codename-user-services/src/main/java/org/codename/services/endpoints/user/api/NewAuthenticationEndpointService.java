/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.user.api;

import java.io.Serializable;
import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codename.core.exceptions.ServiceException;
import org.codename.model.user.User;

/**
 *
 * @author grogdj
 */
@Local
@Path("/auth2")
public interface NewAuthenticationEndpointService extends Serializable {

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response registerUser(@NotNull User user) throws ServiceException;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@NotNull User user) throws ServiceException;

    @POST
    @Path("/logout")
    public Response logout(
            @Context HttpHeaders httpHeaders) throws ServiceException;

}
