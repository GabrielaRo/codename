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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codename.core.exceptions.ServiceException;

/**
 *
 * @author grogdj
 */
@Local
@Path("/public/users")
public interface PublicUserEndpointService extends Serializable {

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON})
    Response getAll() throws ServiceException;
    
    @GET
    @Path("{nickname}")
    @Produces({MediaType.APPLICATION_JSON})
    Response getByNickName(@PathParam("nickname") String nickname) throws ServiceException;
    
    @Path("{nickname}/avatar")
    @GET
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.MEDIA_TYPE_WILDCARD})
    Response getAvatar(@NotNull @PathParam("nickname") String nickname, @QueryParam("size") Integer size) throws ServiceException;
    
    @Path("{nickname}/cover")
    @GET
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.MEDIA_TYPE_WILDCARD})
    Response getCover(@NotNull @PathParam("nickname") String nickname, @QueryParam("size") Integer size) throws ServiceException;
    
   
    
   
    
  

}
