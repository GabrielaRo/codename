/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codename.services.endpoints.chat.api;

import java.io.Serializable;
import javax.ejb.Local;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.codename.core.exceptions.ServiceException;

/**
 *
 * @author grogdj
 */
@Local
@Path("/presence")
public interface PresenceEndpointService extends Serializable {

    
    @GET
    @Path("/notifications")
    @Produces({"application/json"})
    Response getAllNotificationsByUser(@QueryParam("nickname") String nickname) throws ServiceException;
    
    @GET
    @Path("/")
    @Produces({"application/json"})
    Response getUsersState(@QueryParam("users") String users) throws ServiceException;
    
    @PUT
    @Path("/")
    @Produces({"application/json"})
    Response registerInterstInUser(@FormParam("nickname") String nickname, @FormParam("usersNicknames") String usersNicknames);
    

}
