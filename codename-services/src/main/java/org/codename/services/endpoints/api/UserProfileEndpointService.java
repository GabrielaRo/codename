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
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codename.services.exceptions.ServiceException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 *
 * @author salaboy
 */
@Local
@Path("/users")
public interface UserProfileEndpointService extends Serializable {

    @Path("{id}/avatar/upload")
    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    Response uploadAvatar(@NotNull @PathParam("id") Long user_id, MultipartFormDataInput input) throws ServiceException;
    
    @Path("{id}/avatar")
    @GET
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    Response getAvatar(@NotNull @PathParam("id") Long user_id) throws ServiceException;
    
    @Path("{id}/avatar/remove")
    @POST
    Response removeAvatar(@NotNull @PathParam("id") Long user_id) throws ServiceException;

    @Path("{id}/exist")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    Response exist(@NotNull @PathParam("id") Long user_id) throws ServiceException;
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    Response get(@PathParam("id") Long user_id) throws ServiceException;
    
    @Path("/new")
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    Response create(@NotNull @FormParam("user_id") Long user_id) throws ServiceException;
    
    @Path("{id}/update")
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    Response update(@NotNull @PathParam("id") Long user_id, @FormParam("firstname") String username, 
            @FormParam("lastname") String lastname, 
            @FormParam("location") String location, @FormParam("bio") String bio) throws ServiceException;
    
    
    @Path("{id}/interests")
    @GET
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    Response getInterests(@NotNull @PathParam("id") Long user_id) throws ServiceException;
    
     @Path("{id}/interests/update")
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    Response setInterests(@NotNull @PathParam("id") Long user_id, @FormParam("interests") String interests) throws ServiceException;
    
}
