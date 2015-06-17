/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.api;

import java.io.Serializable;
import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codename.services.exceptions.ServiceException;

/**
 *
 * @author salaboy
 */
@Local
@Path("/public/users")
public interface PublicUserEndpointService extends Serializable {

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    Response getAll() throws ServiceException;
    
    @Path("{id}/avatar")
    @GET
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.MEDIA_TYPE_WILDCARD})
    Response getAvatar(@NotNull @PathParam("id") Long user_id) throws ServiceException;
    
    @Path("{id}/cover")
    @GET
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.MEDIA_TYPE_WILDCARD})
    Response getCover(@NotNull @PathParam("id") Long user_id) throws ServiceException;
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    Response get(@PathParam("id") Long user_id) throws ServiceException;
    

}
