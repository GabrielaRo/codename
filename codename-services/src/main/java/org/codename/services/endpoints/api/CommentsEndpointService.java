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
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author grogdj
 */
@Local
@Path("/comments")
public interface CommentsEndpointService extends Serializable {

    @Path("/new")
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    Response newComment(@NotNull @FormParam("user_id") Long userId,
            @FormParam("club_id") Long clubId,
            @FormParam("item_id") Long itemId,
            @NotNull @NotEmpty @FormParam("text") String text) throws ServiceException;

    @GET
    @Path("/all")
    @Produces({"application/json"})
    Response getAllComments() throws ServiceException;
    
    @GET
    @Path("/club/{id}")
    @Produces({"application/json"})
    Response getAllCommentsByClub(@PathParam("id") Long club_id) throws ServiceException;
    
    @GET
    @Path("/item/{id}")
    @Produces({"application/json"})
    Response getAllCommentsByItem(@PathParam("id") Long item_id) throws ServiceException;
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    Response get(@PathParam("id") Long comment_id) throws ServiceException;
    
    @POST
    @Path("{id}/remove")
    @Produces({MediaType.APPLICATION_JSON})
    Response remove(@PathParam("id") Long comment_id) throws ServiceException;
    
    
   
}
